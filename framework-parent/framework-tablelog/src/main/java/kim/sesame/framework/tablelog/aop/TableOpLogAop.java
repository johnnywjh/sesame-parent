package kim.sesame.framework.tablelog.aop;

import kim.sesame.framework.tablelog.annotation.DbOpLog;
import kim.sesame.framework.tablelog.config.OpColumn;
import kim.sesame.framework.tablelog.config.OpTable;
import kim.sesame.framework.tablelog.config.TableOpProperties;
import kim.sesame.framework.tablelog.db.bean.TableOpLog;
import kim.sesame.framework.tablelog.db.service.TableOpLogService;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.utils.UUIDUtil;
import kim.sesame.framework.web.context.UserContext;
import kim.sesame.framework.web.entity.IUser;
import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * https://www.cnblogs.com/dreamfree/p/4102619.html
 */
@CommonsLog
@Aspect
@Component
@ConditionalOnProperty(prefix = "sesame.framework.tablelog", name = "enable", havingValue = "true")
public class TableOpLogAop {

    @Autowired
    private TableOpProperties tableOp;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TableOpLogService tableOpLogService;

    /**
     * 环绕通知
     */
    @Around("@annotation(kim.sesame.framework.tablelog.annotation.DbOpLog)")
    public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable {
        Object result = null;
        // 获取注解
//        MethodSignature sign = (MethodSignature) pjd.getSignature();
//        Method method = sign.getMethod();
//        DbOpLog ann = method.getAnnotation(DbOpLog.class);

        // 获取参数
        Object obj = pjd.getArgs()[0];
        String classPath = obj.getClass().getName();
        OpTable opTable = tableOp.getOpTable(classPath);
        // 1.1如果为空, 直接放行
        if (tableOp.isEnable() == false || opTable == null) {
            return pjd.proceed();
        }
        // 1.2 如果没有配置要监听的列也直接放行
        List<OpColumn> columnList = opTable.getColumnList();
        if (columnList == null || columnList.size() == 0) {
            return pjd.proceed();
        }
        // 2.0

        String serviceClassPath = pjd.getSignature().getDeclaringTypeName();
        String methodName = pjd.getSignature().getName();

        // 2.1 获取参数中 字段的值 不为null 的字段集合
        List<ObjFields> files = getFieldsList(obj);
        if (files == null || files.size() == 0) {
            return pjd.proceed();
        }
        Map<String, ObjFields> fieldMap = getFieldsMap(files);
        // 2.2 判断两个集合中是否有交集
        List<OpColumn> colList = new ArrayList<>();
        columnList.stream().forEach(b -> {
            if (fieldMap.keySet().contains(b.getJavaName())) {
                colList.add(b);
            }
        });
        if (colList == null || colList.size() == 0) {
            return pjd.proceed();
        }

        // 3.0 开始记录日志, 修改之前的
        IUser user = UserContext.getUserContext().getUser();
        String updateId = UUIDUtil.getShortUUID();
        List<TableOpLog> logs = new ArrayList<>();
        for (OpColumn t : colList) {
            ObjFields pk = fieldMap.get(opTable.getPkJava()); // 字段的实际对象
            TableOpLog bean = new TableOpLog();
            bean.setTableName(opTable.getTableName());
            bean.setPkName(opTable.getPkId());
//            bean.setPkJavaName(opTable.getPkJava());
            bean.setPkValue(pk.getValue().toString());

            bean.setFieldName(t.getColumnName());
            bean.setFieldComment(t.getComment());

            ObjFields of = fieldMap.get(t.getJavaName()); // 当前这个字段对象
            if (of.getClazz() == String.class && of.getValue().equals("") && t.isStrNull() == false) {
                continue;
            }
            // 获取修改前的值
            String sql = "select " + bean.getFieldName() + " from " + bean.getTableName() + " where " + bean.getPkName() + "=?";
            Object val = jdbcTemplate.queryForObject(sql, new Object[]{bean.getPkValue()}, Object.class);

            bean.setSelectSql(t.getSelectSql());
            if (StringUtil.isNotEmpty(t.getSelectSql())) {
                val = jdbcTemplate.queryForObject(t.getSelectSql(), new Object[]{val}, String.class);
            }
            // 如果修改前后一样,那么也跳过
            if (val.equals(of.getValue())) {
                continue;
            }
            bean.setValueBefore(val.toString());
            bean.setClassPath(serviceClassPath);
            bean.setMethodName(methodName);
            bean.initCreateAndId(user.getAccount());
            bean.setUpdateId(updateId);

            logs.add(bean);
        }
        // 执行业务方法
        result = pjd.proceed();

        // 记录修改后的值
        logs.stream().forEach(bean -> {
            String sql = "select " + bean.getFieldName() + " from " + bean.getTableName() + " where " + bean.getPkName() + "=?";
            Object val = jdbcTemplate.queryForObject(sql, new Object[]{bean.getPkValue()}, Object.class);
            if (StringUtil.isNotEmpty(bean.getSelectSql())) {
                val = jdbcTemplate.queryForObject(bean.getSelectSql(), new Object[]{val}, String.class);
            }
            bean.setValueAfter(val.toString());
        });

        // 开始记录日志, 判断是否异步
        if (opTable.isSync()) {
            insertLogs(logs);
        } else {
            new Thread(() -> {
                insertLogs(logs);
            }).start();
        }

        return result;
    }

    private void insertLogs(List<TableOpLog> logs) {
        logs.stream().forEach(l -> {
            log.debug(l.toString());
            tableOpLogService.add(l);
        });
    }

    /**
     * 反射中获取所有不为null 的字段的集合
     *
     * @param obj
     * @return
     */
    private List<ObjFields> getFieldsList(Object obj) {
        // https://www.cnblogs.com/JackZed/p/6888668.html
//        getFields()：获得某个类的所有的公共（public）的字段，包括父类中的字段。
//        getDeclaredFields()：获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
        List<ObjFields> list = new ArrayList<>();

        Field[] fields1 = obj.getClass().getSuperclass().getDeclaredFields();
        Field[] fields2 = obj.getClass().getDeclaredFields();

        List<Field> fields = new ArrayList<>();
        Stream.of(fields1, fields2).forEach(field -> {
            Stream.of(field).forEach(f -> {
                boolean flg = true;// 在list中是否存在
                for (Field str : fields) {
                    if (str.getName().equals(f.getName())) {
                        flg = false;
                        break;
                    }
                }
                if (flg) {
                    fields.add(f);
                }
            });
        });

        for (Field field : fields) {
            try {
                String name = field.getName();// 字段名称
                int fieldValue = field.getModifiers(); // 获取字段的修饰符 如：private、static、final等
                //看此修饰符是否为静态(static)
                if (Modifier.isStatic(fieldValue)) {
                    continue;
                }
                // 当isAccessible()的结果是false时不允许通过反射访问该字段
                if (field.isAccessible() == false) {
                    field.setAccessible(true);
                }
                //  获取字段的值
                Object fieldObject = field.get(obj);
                if (fieldObject != null) {
                    list.add(new ObjFields(name, fieldObject, field.getType()));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private Map<String, ObjFields> getFieldsMap(List<ObjFields> list) {
        Map<String, ObjFields> map = new HashMap<>();
        if (list == null || list.size() == 0) {
            return map;
        }
        list.stream().forEach(b -> {
            map.put(b.getName(), b);
        });
        return map;
    }

    @Data
    class ObjFields {
        private String name; // 字段名称
        private Object value; // 字段的值
        private Class clazz;// 字段类型

        public ObjFields() {
        }

        public ObjFields(String name, Object value, Class clazz) {
            this.name = name;
            this.value = value;
            this.clazz = clazz;
        }

    }
}

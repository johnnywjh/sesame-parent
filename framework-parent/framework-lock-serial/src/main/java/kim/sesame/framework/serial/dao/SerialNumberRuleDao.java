package kim.sesame.framework.serial.dao;

import kim.sesame.framework.serial.entity.SerialNumberRuleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 序列号Dao
 **/
@Component
public class SerialNumberRuleDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /* 检查表 如果不存在就创建 */
    public void check_notExistsCreate() {
        String sql = " CREATE TABLE  IF NOT EXISTS `sys_serial_number_rule` (    " +
                "          `code` VARCHAR(50) NOT NULL COMMENT '编码',    " +
                "          `name` VARCHAR(400) NOT NULL COMMENT '名称',    " +
                "          `cur_time` DATETIME NOT NULL COMMENT '当前时间',    " +
                "          `cur_num` DECIMAL(18,0) NOT NULL COMMENT '当前序号',    " +
                "          PRIMARY KEY (`code`)    " +
                "        ) DEFAULT CHARSET=utf8mb4 COMMENT='序列号表'";
        jdbcTemplate.execute(sql);
    }

    /* 根据code查询序号实体并添加悲观锁 */
    public SerialNumberRuleEntity querySerialNumberRuleForUpdate(String code) {
        SerialNumberRuleEntity entity = null;
        try {
            String sql = "SELECT     code code,name name,cur_time curTime,cur_num curNum " +
                    "        FROM sys_serial_number_rule    " +
                    "        WHERE CODE = '" + code + "' for update ";

            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
            if(list.size()>0){
                Map map = list.get(0);
                entity = new SerialNumberRuleEntity();
                entity.setCode(map.get("code").toString());
                entity.setName(map.get("name").toString());
                entity.setCurTime((Date)map.get("curTime"));
                BigDecimal bd = (BigDecimal) map.get("curNum");
                entity.setCurNum(bd.longValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    /* 新增序号信息*/
    public void addSerialNumberRule(SerialNumberRuleEntity bean) {
        String sql = " INSERT INTO sys_serial_number_rule " +
                " (code,name,cur_time,cur_num) values " +
                " (?,?,?,?)";
        jdbcTemplate.update(sql, new Object[]{
                bean.getCode(), bean.getName(), bean.getCurTime(), bean.getCurNum()
        });
    }

    /* 更新序号信息*/
    public void updateSerialNumberRule(SerialNumberRuleEntity bean) {
        String sql = "UPDATE sys_serial_number_rule    " +
                "        SET    " +
                "        cur_time = ?,    " +
                "        cur_num = ?    " +
                "        WHERE CODE = ?";
        jdbcTemplate.update(sql, new Object[]{
                bean.getCurTime(), bean.getCurNum(), bean.getCode()
        });
    }
}

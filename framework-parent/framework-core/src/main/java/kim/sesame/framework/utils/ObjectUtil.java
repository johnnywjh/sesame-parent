package kim.sesame.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by johnny on 2017/9/10.
 */
public class ObjectUtil {

    /**
     * 对象转化成Map
     *
     * @param obj 传入的对象
     * @return 返回map
     */
    public static Map ObjectToGMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        try {
            Class<?> cls = obj.getClass();
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                String strGet = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
                Method methodGet = cls.getDeclaredMethod(strGet);
                Object object = methodGet.invoke(obj);
                Object value = object != null ? object : "";
                map.put(name, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * obj 非空校验,包含字符串的校验
     * isNotEmpty(new Object())// true
     * isNotEmpty("111") // ture
     * isNotEmpty(new Date()) // ture
     *
     * isNotEmpty("") // false
     * isNotEmpty(null) // false
     */
    public static boolean isNotEmpty(Object obj) {
        if (obj == null || (obj instanceof String && obj.equals(""))) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * obj 对象为空的判断,包含字符串的校验
     */
    public static boolean isEmpty(Object obj) {
        return !isNotEmpty(obj);
    }

}

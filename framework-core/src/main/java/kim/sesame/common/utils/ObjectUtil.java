package kim.sesame.common.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by johnny on 2017/9/10.
 */
public class ObjectUtil extends ObjectUtils {

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

}

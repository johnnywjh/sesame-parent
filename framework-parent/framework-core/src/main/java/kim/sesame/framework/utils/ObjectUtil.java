package kim.sesame.framework.utils;

import kim.sesame.framework.entity.GMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by johnny on 2017/9/10.
 */
public class ObjectUtil {

	/**
	 * http://blog.csdn.net/tarrant1/article/details/10954633
	 *
	 * @param obj
	 * @return
	 */
	public static GMap ObjectToGMap(Object obj) {
		GMap gMap = null;
		try {
			gMap = GMap.newMap();
			Class<?> cls = obj.getClass();
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				String strGet = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
				Method methodGet = cls.getDeclaredMethod(strGet);
				Object object = methodGet.invoke(obj);
				Object value = object != null ? object : "";
				gMap.put(name, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return gMap;
	}

	public static void main(String[] args) {
		List<User> list = Arrays.asList(new User(1, "aaa"), new User(2, "bbb"));
		List<GMap> list_map = new ArrayList<>();
		list.forEach(bean -> {
			list_map.add(ObjectToGMap(bean));
		});

		list_map.forEach(System.out::println);
	}

	public static class User {
		private int id;
		private String name;

		public User() {
		}

		public User(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}

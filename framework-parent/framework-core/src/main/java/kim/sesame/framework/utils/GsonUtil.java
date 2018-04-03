package kim.sesame.framework.utils;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

/**
 * gson 工具类
 */
public class GsonUtil {

    public static Gson getGson() {
        return new GsonBuilder().serializeNulls().create();
    }

    public static String toJson(List list) {
        return getGson().toJson(list);
    }

    public static <T> ArrayList<T> fromJsonList(String json, Class<T> cls) {
        Gson gson = GsonUtil.getGson();
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            mList.add(gson.fromJson(elem, cls));
        }
        return mList;
    }

}

package kim.sesame.common.utils;

import java.util.UUID;

public class UUIDUtil {
    public UUIDUtil() {
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getShortUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
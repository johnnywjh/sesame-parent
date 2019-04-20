package kim.sesame.framework.web.config;

public class ProjectConfig {
    private static String sysCode;
    private static String superAdmin;
    private static boolean debug;
    private static String systemExceptionMessage;

    static void setSysCode(String sysCode) {
        ProjectConfig.sysCode = sysCode;
    }

    static void setSuperAdmin(String superAdmin) {
        ProjectConfig.superAdmin = superAdmin;
    }

    public static String getSysCode() {
        return sysCode;
    }

    public static String getSuperAdmin() {
        return superAdmin;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        ProjectConfig.debug = debug;
    }

    public static String getSystemExceptionMessage() {
        return systemExceptionMessage;
    }

    public static void setSystemExceptionMessage(String systemExceptionMessage) {
        ProjectConfig.systemExceptionMessage = systemExceptionMessage;
    }
}

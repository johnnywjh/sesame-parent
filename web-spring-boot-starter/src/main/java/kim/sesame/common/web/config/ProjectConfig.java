package kim.sesame.common.web.config;

public class ProjectConfig {
    private static String sysCode;// 系统编号
    private static String superAdmin;// 超级管理员用户账号
    private static boolean debug;// 是否debug模式
    private static String systemExceptionMessage;// 系统异常消息

    static void setSysCode(String sysCode) {
        ProjectConfig.sysCode = sysCode;
    }

    static void setSuperAdmin(String superAdmin) {
        ProjectConfig.superAdmin = superAdmin;
    }

    static void setDebug(boolean debug) {
        ProjectConfig.debug = debug;
    }

    static void setSystemExceptionMessage(String systemExceptionMessage) {
        ProjectConfig.systemExceptionMessage = systemExceptionMessage;
    }

    /*---------------------------*/

    public static String getSysCode() {
        return sysCode;
    }

    public static String getSuperAdmin() {
        return superAdmin;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static String getSystemExceptionMessage() {
        return systemExceptionMessage;
    }

}

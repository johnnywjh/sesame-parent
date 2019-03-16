package kim.sesame.framework.web.config;

public class ProjectConfig {
    private static String sysCode;
    private static String superAdmin;

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

}

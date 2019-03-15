package kim.sesame.framework.web.config;

public class ProjectConfig {
    private static String sysCode;
    private static String superAdmin;
    private static String uploadUrl;

    static void setSysCode(String sysCode) {
        ProjectConfig.sysCode = sysCode;
    }

    static void setSuperAdmin(String superAdmin) {
        ProjectConfig.superAdmin = superAdmin;
    }

    static void setUploadUrl(String uploadUrl) {
        ProjectConfig.uploadUrl = uploadUrl;
    }

    public static String getSysCode() {
        return sysCode;
    }

    public static String getSuperAdmin() {
        return superAdmin;
    }

    public static String getUploadUrl() {
        return uploadUrl;
    }
}

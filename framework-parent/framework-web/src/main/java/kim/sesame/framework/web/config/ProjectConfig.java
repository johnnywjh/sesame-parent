package kim.sesame.framework.web.config;

public class ProjectConfig {
    private static String sysCode;
    private static String spuerAdmin;
    private static String uploadUrl;

    static void setSysCode(String sysCode) {
        ProjectConfig.sysCode = sysCode;
    }

    static void setSpuerAdmin(String spuerAdmin) {
        ProjectConfig.spuerAdmin = spuerAdmin;
    }

    static void setUploadUrl(String uploadUrl) {
        ProjectConfig.uploadUrl = uploadUrl;
    }

    public static String getSysCode() {
        return sysCode;
    }

    public static String getSpuerAdmin() {
        return spuerAdmin;
    }

    public static String getUploadUrl() {
        return uploadUrl;
    }
}

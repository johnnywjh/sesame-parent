package kim.sesame.framework.web.config;

public class ProjectConfig {
    private static String sysCode;
    private static String spuerAdmin;
    private static String uploadUrl;
    private static String fileMapping;

    static void setSysCode(String sysCode) {
        ProjectConfig.sysCode = sysCode;
    }

    static void setSpuerAdmin(String spuerAdmin) {
        ProjectConfig.spuerAdmin = spuerAdmin;
    }

    static void setUploadUrl(String uploadUrl) {
        ProjectConfig.uploadUrl = uploadUrl;
    }

    static String getFileMapping() {
        return fileMapping;
    }

    static void setFileMapping(String fileMapping) {
        ProjectConfig.fileMapping = fileMapping;
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

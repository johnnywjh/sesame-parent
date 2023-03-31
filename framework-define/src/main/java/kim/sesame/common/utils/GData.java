package kim.sesame.common.utils;

/**
 * 系统常量
 * 特殊的常量建议在子项目中复制一份, 并修改java类名
 */
public class GData {

    public interface Log {
        String LOG_PREFIX = "logPrefix";
    }

    public interface JWT {
        String SESSION_ID = "jwt_session_id";

        String ACC_LOAD = "acc_load";

        /** @see kim.sesame.common.entity.JwtUser ## loginUserId */
        String USER_ID = "loginUserId";
        String ACCOUNT = "account";
        String NAME = "name";
        String PWD_VERSION = "pwd_version";
        String COMPARE_PWD_VERSION = "compare_pwd_version";

        String TOKEN = "access_token";
    }

    /**
     *
     */
    public interface LOGIN {
        /**
         * 登录后,用户账号在缓存中的key
         */
        String USER_ACCOUNT_KEY = "kim.cache.user.login_id";
        /**
         * 用户对象在缓存里的key
         */
        String USER_INFO_KEY = "kim.cache.user.info";
        /**
         * 用户角色信息在缓存里的key
         */
        String USER_ROLE_KEY = "kim.cache.user.role";
        /**
         * 用户菜单在缓存里的key
         */
        String USER_MENU_KEY = "kim.cache.user.menu";
    }

    /**
     * 本地缓存中的key,
     */
    public interface LOCALCACHE {
        String WEB_CACHE_USER_INFO = "webCacheUserInfo";
        String WEB_CACHE_USER_ROLE = "webCacheUserRole";
        String WEB_CACHE_USER_MENU = "webCacheUserMenu";
    }

    /**
     * 时间
     */
    public interface DATE {
        /**
         * 日期格式
         */
        String DATE_FORMATTER = "yyyy-MM-dd";
        /**
         * 时间格式
         */
        String DATETIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";
        /**
         * 日期小时
         */
        String DATE_HOUR_FORMATTER = "yyyy-MM-dd HH:mm";

        /**
         * 日期月份
         */
        String DATE_MONTH_FORMATER = "yyyy-MM";
    }

    public static String userCacheId(String sessionId) {
        return LOGIN.USER_ACCOUNT_KEY + "_" + sessionId;
    }


}

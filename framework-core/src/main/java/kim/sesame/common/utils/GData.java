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
        String ACCOUNT = "account";
        String NAME = "name";
        String PWD_VERSION = "pwd_version";
        String COMPARE_PWD_VERSION = "compare_pwd_version";

        String TOKEN = "token";
    }

    /**
     * 保存在session里面的key
     */
    public static interface SESSION {
        /**
         * 登录
         */
        String LOGIN = "sysuserkey";
        /**
         * 菜单
         */
        String MENU = "menulistkey";
    }

    /**
     * 本地缓存中的key,
     */
    public interface LOCALCACHE {
        String WEB_CACHE_USER_INFO = "webCacheUserInfo";
        String WEB_CACHE_USER_ROLE = "webCacheUserRole";
        String WEB_CACHE_USER_MENU = "webCacheUserMenu";
    }

}

package kim.sesame.framework.utils;

/**
 * 系统常量
 *
 * @author johnny
 * date :  2017年4月16日 下午4:14:34
 * Description: 特殊的常量建议在子项目中复制一份, 并修改java类名
 */
public class GData {

    public static interface SPRINGBOOT {
        /**
         * spirng boot 1.x : server.context-path
         * spirng boot 2.x : server.servlet.context-path
         */
        String contextPath = "server.context-path";
    }

    public static interface JWT {
        String SESSION_ID = "jwt_session_id";
        String TOKEN = "accessToken";
    }

    /**
     * 系统常量
     */
    public static interface SYS {
        /**
         * 超级管理员用户账号
         */
        String SPUER_ADMIN_USER = "spuer_admin";
        /**
         * 超级管理员角色编号
         */
        String SPUER_ADMIN_ROLE = "spuer_admin";
        /**
         * 管理员用户账号
         */
        String ADMIN_USER = "admin";
        /**
         * 管理员角色编号
         */
        String ADMIN_ROLE = "admin";

        /**
         * 默认头像,好像有点丑
         */
        String DEFAULT_TX_IMG = "http://www.sesame.kim/images/default_tx.png";
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
     * 是否标识: Y 是,N 否
     */
    public static interface BOOLEAN {
        /**
         * Y 是
         */
        String YES = "Y";
        /**
         * N 否
         */
        String NO = "N";
    }

    /**
     * 性别 : 1 男,2 女,3 未知
     */
    public static interface SEX {
        /**
         * 1 男
         */
        String MAN = "1";
        /**
         * 2 女
         */
        String WOMAN = "2";
        /**
         * 3 未知
         */
        String NONE = "3";
    }

    /**
     * spring cloud 里的常量
     */
    public static interface CLOUD {
        /**
         * zuul 里的sessionid 的key
         */
        String ZUUL_SESSION_ID = "zuulSessionId";
    }

}

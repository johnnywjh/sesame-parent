package com.sesame.framework.utils;

/**
 * 系统常量
 *
 * @author wangjianghai
 * @date 2017年4月16日 下午4:14:34
 * @Description: 特殊的常量建议在子项目中复制一份, 并修改java类名
 */
public class GData {

    /**
     * 系统常量
     */
    public static interface SYS {
        /**
         * 超级管理员角色id,一般情况是1
         */
        static final String ADMIN_ROLEID = "1";
    }

    /**
     * 保存在session里面的key
     */
    public static interface SESSION {
        /**
         * 登录
         */
        static final String LOGIN = "sysuserkey";
        /**
         * 菜单
         */
        static final String MENU = "menulistkey";
    }

    /**
     * 是否标识: Y 是,N 否
     */
    public static interface BOOLEAN {
        /**
         * Y 是
         */
        static final String YES = "Y";
        /**
         * N 否
         */
        static final String NO = "N";
    }

    /**
     * 性别 : 1 男,2 女,3 未知
     */
    public static interface SEX {
        /**
         * 1 男
         */
        static final String MAN = "1";
        /**
         * 2 女
         */
        static final String WOMAN = "2";
        /**
         * 3 未知
         */
        static final String NONE = "3";
    }

    /**
     * spring cloud 里的常量
     */
    public static interface CLOUD {
        /**
         * zuul 里的sessionid 的key
         */
        static final String ZUUL_SESSION_ID = "zuulSessionId";
    }

}

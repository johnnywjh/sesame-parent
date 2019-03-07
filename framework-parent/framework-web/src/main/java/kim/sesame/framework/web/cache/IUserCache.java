package kim.sesame.framework.web.cache;

import kim.sesame.framework.cache.define.IStringCacheTemplate;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.config.WebProperties;
import kim.sesame.framework.web.context.SpringContextUtil;
import kim.sesame.framework.web.entity.IRole;
import kim.sesame.framework.web.entity.IUser;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户缓存Cache接口
 **/
public interface IUserCache {

    /**
     * 子类在spring 容器中的注册名字
     */
    // String USER_LOGIN_BEAN = "user_cache_bean";
    /**
     * 登录后,用户账号在缓存中的key
     */
    String USER_ACCOUNT_KEY = "kim.sesame.cache.user.account";
    /**
     * 用户对象在缓存里的key
     */
    String USER_INFO_KEY = "kim.sesame.cache.user.info";
    /**
     * 用户角色信息在缓存里的key
     */
    String USER_ROLE_KEY = "kim.sesame.cache.user.role";
    /**
     * 用户菜单在缓存里的key
     */
    String USER_MENU_KEY = "kim.sesame.cache.user.menu";

    /**
     * 获取登录用户在缓存里的key
     *
     * @param sessionId sessionid
     * @return
     */
    default String userCacheId(String sessionId) {
        return USER_ACCOUNT_KEY + "_" + sessionId;
    }

    /**
     * 添加用户信息到缓存
     *
     * @param user      用户信息
     * @param sessionId sessionId
     */
    default void addUsersToCache(String user, String sessionId) {
        IStringCacheTemplate stringCache = SpringContextUtil.getBean(IStringCacheTemplate.class);
        WebProperties web = SpringContextUtil.getBean(WebProperties.class);
        if (stringCache == null) {
            throw new NullPointerException("请实现 IStringCacheTemplate 接口");
        }
        if (web == null) {
            throw new NullPointerException("扫描问题,无法装配 WebProperties");
        }

        String key = userCacheId(sessionId);
        String value = user;
        int time = web.getUserLoginSaveTime();
        System.out.println(MessageFormat.format("登录成功,添加到缓存 : [key] : {0} , [value] : {1} , [time] : {2} 分钟", key, value, time));
        stringCache.set(key, value, time, TimeUnit.MINUTES);
    }

    /**
     * 从缓存中删除用户信息
     *
     * @param sessionId sessionid
     */
    default void invalidUserCache(String sessionId) {
        IStringCacheTemplate stringCache = SpringContextUtil.getBean(IStringCacheTemplate.class);
        if (stringCache == null) {
            throw new NullPointerException("请实现 IStringCacheTemplate 接口");
        }
        String key = userCacheId(sessionId);
        boolean res = stringCache.delete(key);
        res = stringCache.delete(key);
        res = stringCache.delete(key);
    }

    /**
     * 从缓存中获取用户账号
     *
     * @param sessionId sessionId
     * @return
     */
    default String getUserNo(String sessionId) {
        IStringCacheTemplate stringCache = SpringContextUtil.getBean(IStringCacheTemplate.class);
        WebProperties web = SpringContextUtil.getBean(WebProperties.class);
        if (stringCache == null) {
            throw new NullPointerException("请实现 IStringCacheTemplate 接口");
        }
        if (web == null) {
            throw new NullPointerException("扫描问题,无法装配 WebProperties");
        }

        String key = userCacheId(sessionId);
        try {
            String userCode = stringCache.get(key);
            // 当获取用户编号完成之后,再存一次到缓存中,延续登录
            new Thread(() -> {
                if (StringUtil.isNotEmpty(userCode)) {
                    int time = web.getUserLoginSaveTime();
                    stringCache.set(key, userCode, time, TimeUnit.MINUTES);
                }
            }).start();
            return userCode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取用户对象
     *
     * @param userNo 用户账号
     * @return 用户信息
     */
    IUser getUserCache(String userNo);

    /**
     * 获取用户角色信息
     *
     * @param userNo 用户账号
     * @return 用户角色信息的集合
     */
    List<IRole> getUserRoles(String userNo);
}

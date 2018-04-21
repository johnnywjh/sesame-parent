package kim.sesame.framework.web.cache;

import kim.sesame.framework.web.entity.IRole;
import kim.sesame.framework.web.entity.IUser;

import java.util.List;

/**
 * 用户缓存Cache接口
 **/
public interface IUserCache {

    /**
     * 子类在spring 容器中的注册名字
     */
    String USER_LOGIN_BEAN = "user_cache_bean";
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
     * 获取登录用户在缓存里的key
     *
     * @param sessionId sessionid
     * @return
     */
    String userCacheId(String sessionId);

    /**
     * 添加用户信息到缓存
     *
     * @param user      用户信息
     * @param sessionId sessionId
     */
    void addUsersToCache(String user, String sessionId);

    /**
     * 从缓存中删除用户信息
     *
     * @param sessionId sessionid
     */
    void invalidUserCache(String sessionId);

    /**
     * 从缓存中获取用户账号
     *
     * @param sessionId sessionId
     * @return
     */
    String getUserNo(String sessionId);

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

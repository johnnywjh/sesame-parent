package com.sesame.framework.web.cache;

import com.sesame.framework.web.entity.IUser;

/**
 * 用户缓存Cache接口
 *
 **/
public interface IUserCache {

    String  USER_LOGIN_BEAN ="user_cache_bean";

    String userCacheId(String sessionId);

    void addUsersToCache(String user, String sessionId) ;

     String getUserNo(String sessionId);

     IUser getUserCache(String userNo);

    Object getUserRole(String userNo);
}

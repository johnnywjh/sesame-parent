package kim.sesame.framework.web.cache;

import kim.sesame.framework.web.entity.IUser;

import java.util.List;

/**
 * 用户缓存的空实现
 */
public class AbstractUserCache implements IUserCache {
    @Override
    public String userCacheId(String sessionId) {
        return USER_ACCOUNT_KEY + "_" + sessionId;
    }

    @Override
    public void addUsersToCache(String user, String sessionId) {

    }

    @Override
    public void invalidUserCache(String sessionId) {

    }

    @Override
    public String getUserNo(String sessionId) {
        return null;
    }

    @Override
    public IUser getUserCache(String userNo) {
        return null;
    }

    @Override
    public List getUserRoles(String userNo) {
        return null;
    }
}

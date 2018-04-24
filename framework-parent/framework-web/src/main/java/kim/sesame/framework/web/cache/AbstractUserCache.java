package kim.sesame.framework.web.cache;

import kim.sesame.framework.web.entity.IRole;
import kim.sesame.framework.web.entity.IUser;

import java.util.List;

/**
 * 用户缓存的空实现
 * 这个类将被废弃掉,请直接实现 IUserCache这个接口
 */
@Deprecated
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
    public List<IRole> getUserRoles(String userNo) {
        return null;
    }
}

package kim.sesame.framework.web.context;

import kim.sesame.framework.web.entity.IRole;
import kim.sesame.framework.web.entity.IUser;

import java.util.List;

/**
 * UserContext
 *
 * @author johnny
 * date :  2017-11-07 13:25
 * Description:用户上下文
 */
public class UserContext {

    private final static ThreadLocal<IUser> USER_THREAD_LOCAL = new ThreadLocal<>();
    private final static ThreadLocal<String> SESSIONID = new ThreadLocal<>();
    private final static ThreadLocal<List<IRole>> USER_ROLE = new ThreadLocal<>();

    //构造器私有化
    private UserContext() {
    }

    //单例
    private static class ContextHolder {
        private final static UserContext userContext = new UserContext();
    }

    public static UserContext getUserContext() {
        return ContextHolder.userContext;
    }

    /*-------------------------------------------------------*/
    public IUser getUser() {
        return USER_THREAD_LOCAL.get();
    }

    public <T> T getUser(Class<T> objectType) {
        return (T) USER_THREAD_LOCAL.get();
    }

    public void setUser(IUser user) {
        USER_THREAD_LOCAL.set(user);
    }

    /*-------------------------------------------------------*/
    public String getUserSessionId() {
        return SESSIONID.get();
    }

    public void setUserSessionId(String sessionId) {
        SESSIONID.set(sessionId);
    }

    /*-------------------------------------------------------*/
    public List<IRole> getUserRole() {
        return USER_ROLE.get();
    }

    public <T> T getUserRole(Class<T> objectType) {
        return (T) USER_ROLE.get();
    }

    public void setUserRole(List roles) {
        USER_ROLE.set(roles);
    }


    /*-------------------------------------------------------*/
    public void clean() {
        USER_THREAD_LOCAL.remove();
        SESSIONID.remove();
        USER_ROLE.remove();
    }
}
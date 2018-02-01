package kim.sesame.framework.cache.util;

import kim.sesame.framework.cache.IFunctionCache;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.context.SpringContextUtil;

/**
 * Cache工具类
 **/
public class CacheUtil {
    private static String userCacheId = null;

    private static String functionCacheId = null;
    /**
     * 获取用户缓存ID
     * @return
     */
//    public static String getUserCacheId(){
//        if(StringUtil.isNotBlank(userCacheId)){
//            return userCacheId;
//        }
////        IUserCache userCache = WebApplicationContextHolder.getWebApplicationContext().getBean(IUserCache.class);
//        IUserCache userCache = (IUserCache) SpringContextUtil.getBean(IUserCache.class);
//        userCacheId = userCache.getClass().getName();
//        return userCacheId;
//    }

    /**
     * 获取权限缓存ID
     * @return
     */
    public static String getFunctionCacheId(){
        if(StringUtil.isNotBlank(functionCacheId)){
            return functionCacheId;
        }
//        IFunctionCache functionCache = WebApplicationContextHolder.getWebApplicationContext().getBean(IFunctionCache.class);
        IFunctionCache functionCache = (IFunctionCache) SpringContextUtil.getBean(IFunctionCache.class);
        functionCacheId = functionCache.getClass().getName();
        return functionCacheId;
    }
}

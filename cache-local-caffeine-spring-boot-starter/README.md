### 多个缓存方案

#### 1. 配置
- LocalTTLCacheEntity.java
```java
package kim.sesame.framework.cache.local.caffeine.config;

import lombok.Data;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存-配置,实体
 */
@Data
public class LocalTTLCacheEntity {
    private String name; // 缓存注解上的名字
    private int ttl = 3600;//过期时间（秒）
    private TimeUnit timeUnit = TimeUnit.SECONDS;// 时间单位默认秒
    private int maxSize = 1000;//最大數量


    public LocalTTLCacheEntity() {
    }

    public LocalTTLCacheEntity(String name) {
        this.name = name;
    }

    public LocalTTLCacheEntity(String name, int ttl) {
        this.name = name;
        this.ttl = ttl;
    }

    public LocalTTLCacheEntity(String name, int ttl, TimeUnit timeUnit) {
        this.name = name;
        this.ttl = ttl;
        this.timeUnit = timeUnit;
    }

    public LocalTTLCacheEntity(String name, int ttl, TimeUnit timeUnit, int maxSize) {
        this.name = name;
        this.ttl = ttl;
        this.timeUnit = timeUnit;
        this.maxSize = maxSize;
    }

    public LocalTTLCacheEntity(String name, int ttl, int maxSize) {
        this.name = name;
        this.ttl = ttl;
        this.maxSize = maxSize;
    }

    @Override
    public String toString() {
        return "LocalTTLCacheEntity{" +
                "name='" + name + '\'' +
                ", ttl=" + ttl +
                ", timeUnit=" + timeUnit +
                ", maxSize=" + maxSize +
                '}';
    }
}

```

- LocalTTlCacheAbstract.java
```java
package kim.sesame.framework.cache.local.caffeine.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存配置父类
 */
/* 例子
  @Component  // 一定要加入spring容器中
  public class CacheImpl1 extends LocalTTlCacheAbstract {
      @Override
      protected void setConfigs() {
          configs = Arrays.asList(
                  new LocalTTLCacheEntity("userlist", 10)  // 第一个参数是 @Cacheable 注解的 value 值
          );
      }
  }
* */
@Slf4j
@Component
public class LocalTTlCacheAbstract implements InitializingBean {

    protected List<LocalTTLCacheEntity> configs = null;

    public List<LocalTTLCacheEntity> getConfigs() {
        if (configs == null) {
            return new ArrayList();
        }
        return configs;
    }

    /**
     * 给子类重写
     */
    protected void setConfigs() {
        configs = new ArrayList<>();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        setConfigs();
    }
}

```

- LocalCacheConfig.java
```java
package kim.sesame.framework.cache.local.caffeine.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import kim.sesame.framework.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>java方式：caffeine缓存配置</p>
 * Created by zhezhiyong@163.com on 2017/9/22.
 */
@Slf4j
@Configuration
@EnableCaching
public class LocalCacheConfig {

    /**
     * 个性化配置缓存
     */
    @SuppressWarnings("all")
    @Bean
    public CacheManager cacheManager(List<LocalTTlCacheAbstract> cacheConfigs) {
        SimpleCacheManager manager = new SimpleCacheManager();
        //把各个cache注册到cacheManager中，CaffeineCache实现了org.springframework.cache.Cache接口
        ArrayList<CaffeineCache> caches = new ArrayList<>();
        log.info("---- 本地缓存 TTL 配置 -----------------");
        List<String> cacheNames = new ArrayList<>();// 保存所有加载的缓存名字
        cacheConfigs.forEach(config -> {
            config.getConfigs().forEach(c -> {
                // 判断是否有重复
                if (cacheNames.contains(c.getName())) {
                    throw new BusinessException(MessageFormat.format("LocalTTLConfig有重复的名称:{0},类名:{1}", c.getName(), config.getClass()));
                } else {
                    cacheNames.add(c.getName());
                    log.info(c);
                    caches.add(new CaffeineCache(c.getName(),
                            Caffeine.newBuilder().recordStats()
                                    .expireAfterWrite(c.getTtl(), c.getTimeUnit())
                                    .maximumSize(c.getMaxSize())
                                    .build())
                    );
                }
            });
        });
        log.info("---------------------");
        manager.setCaches(caches);
        return manager;
    }


}

```

#### 2. 使用
- UserCacheConfig.java
```java
package com.jfyl.mq.config;

import kim.sesame.framework.cache.local.caffeine.config.LocalTTLCacheEntity;
import kim.sesame.framework.cache.local.caffeine.config.LocalTTlCacheAbstract;
import kim.sesame.framework.utils.GData;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 用户本地缓存配置
 */
@Component
public class UserCacheConfig extends LocalTTlCacheAbstract {

    @Override
    protected void setConfigs() {
        configs = Arrays.asList(
                new LocalTTLCacheEntity(GData.LOCALCACHE.WEB_CACHE_USER_INFO, 1, TimeUnit.HOURS)
                , new LocalTTLCacheEntity(GData.LOCALCACHE.WEB_CACHE_USER_ROLE, 1, TimeUnit.HOURS)
                , new LocalTTLCacheEntity(GData.LOCALCACHE.WEB_CACHE_USER_MENU, 1, TimeUnit.HOURS)
        );
    }

}

```

```java
package com.jfyl.framework.usercache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jfyl.jf.sys.entity.JfSysMenu;
import com.jfyl.jf.sys.entity.JfSysRole;
import com.jfyl.jf.sys.entity.JfSysUser;
import com.jfyl.jf.sys.mapper.JfSysMenuMapper;
import com.jfyl.jf.sys.service.JfSysMenuService;
import com.jfyl.jf.sys.service.JfSysRoleService;
import com.jfyl.jf.sys.service.JfSysUserService;
import kim.sesame.framework.cache.annotation.QueryCache;
import kim.sesame.framework.cache.redis.aop.QueryCacheAop;
import kim.sesame.framework.utils.GData;
import kim.sesame.framework.web.cache.IUserCache;
import kim.sesame.framework.web.context.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SysCacheServer {

    @Autowired
    JfSysUserService userService;
    @Autowired
    JfSysRoleService roleService;
    @Autowired
    JfSysMenuService menuService;
    @Autowired
    JfSysMenuMapper menuMapper;
    /*
     * ##############################################################
     * -----------  本地缓存
     */

    /**
     * 用户信息本地缓存
     *
     * @param userNo 用户编号
     * @return
     */
    @Cacheable(value = GData.LOCALCACHE.WEB_CACHE_USER_INFO, key = "#userNo")
    public JfSysUser getLocalUserInfoCache(String userNo) {
        return SpringContextUtil.getBean(this.getClass()).queryUserByAccount(userNo);
    }

    // 清除用户信息本地缓存
    @CacheEvict(value = GData.LOCALCACHE.WEB_CACHE_USER_INFO, key = "#userNo")
    public void clearLocalUserInfo(String userNo) {
        log.debug("清除用户信息本地缓存 : " + userNo);
    }

    /**
     * 角色信息本地缓存
     *
     * @param userNo 用户编号
     * @return
     */
    @Cacheable(value = GData.LOCALCACHE.WEB_CACHE_USER_ROLE, key = "#userNo")
    public List<JfSysRole> getLocalUserRoleCache(String userNo) {
        return SpringContextUtil.getBean(this.getClass()).queryRolesByUserno(userNo);
    }

    // 清除角色信息本地缓存
    @CacheEvict(value = GData.LOCALCACHE.WEB_CACHE_USER_ROLE, key = "#userNo")
    public void clearLocalCacheUserRole(String userNo) {
        log.debug("清除角色信息本地缓存 : " + userNo);
    }

    /*
     * ##############################################################
     * -----------  redis 查询缓存
     */

    /**
     * 用户信息查询
     *
     * @param account 用户账号
     * @return
     */
    @QueryCache(key = IUserCache.USER_INFO_KEY)
    public JfSysUser queryUserByAccount(String account) {
        JfSysUser user = userService.getOne(new LambdaQueryWrapper<JfSysUser>()
                .eq(JfSysUser::getDeleteFlag, false)
                .eq(JfSysUser::getAccount, account)
        );
        userService.initUserInfo(user);
        // 对微信昵称进行解码
        user.wxnameDecode();
        // 不返回密码
        user.setPwd("");
        return user;
    }

    // 清除用户信息 缓存
    public void queryUserByAccount_invalid(String account) {
        //1 清除redis里的缓存
        String classPath = this.getClass().getName();
        QueryCacheAop.invalid(IUserCache.USER_INFO_KEY, null, classPath, "queryUserByAccount", new Object[]{account});

        //2 清除本地缓存
        SpringContextUtil.getBean(this.getClass()).clearLocalUserInfo(account);
    }

    /**
     * 角色信息查询
     *
     * @param userNo 用户编号
     * @return
     */
    @QueryCache(key = IUserCache.USER_ROLE_KEY)
    public List<JfSysRole> queryRolesByUserno(String userNo) {
        return roleService.queryRolesByUserNo(userNo);
    }

    // 清理角色信息缓存
    public void queryRolesByUserno_invalid(String userNo) {
        // 清除redis里的缓存
        String classPath = this.getClass().getName();
        QueryCacheAop.invalid(IUserCache.USER_ROLE_KEY, null, classPath, "queryRolesByUserno", new Object[]{userNo});
        // 清除本地缓存
        SpringContextUtil.getBean(this.getClass()).clearLocalCacheUserRole(userNo);
    }

    /*
     * ##############################################################
     * -----------  其他方法
     */


    //    @QueryCache(isWriteNullValueToJson = false)
    public List<JfSysMenu> queryMenuByUserCode(String account, String systemCode, String type) {

        Map params = new HashedMap();
        params.put("systemCode", systemCode);
        params.put("type", type);
        params.put("account", account);

        List<JfSysMenu> list = menuMapper.getMenuByUserCode(params);
        return list;
    }

    public void queryMenuByUserCode_invalid(String account, String systemCode, String type) {
        String classPath = this.getClass().getName();
        QueryCacheAop.invalid(null, null, classPath, "queryMenuByUserCode", new Object[]{account, systemCode, type});
    }


    public void getUserList_invalid(String account) {
        String classPath = this.getClass().getName();
        QueryCacheAop.invalid(null, null, classPath, "getUserList", new Object[]{account});
    }
}

```
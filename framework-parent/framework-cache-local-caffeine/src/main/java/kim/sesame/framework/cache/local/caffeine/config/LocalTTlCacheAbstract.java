package kim.sesame.framework.cache.local.caffeine.config;

import lombok.extern.apachecommons.CommonsLog;
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
@CommonsLog
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

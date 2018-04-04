package kim.sesame.framework.cache.redis.server;

import kim.sesame.framework.utils.GsonUtil;
import kim.sesame.framework.utils.StringUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@CommonsLog
public class AutoRefreshCache<T> implements InitializingBean {

    protected StringRedisTemplate stringRedisTemplate;
    protected int refreshTime;
    protected Class clazz = Object.class;
    protected boolean state = false; //get 方法是否可以访问
    protected boolean openCache = true; //是否开启缓存

    protected List<T> dataLoading() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (openCache == false) {
            return;
        }
        Thread t = new Thread(() -> {
            while (true) {
                refresh();
                try {
                    TimeUnit.SECONDS.sleep(refreshTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        if (Thread.currentThread().getName().equals("main")) {
            t.setDaemon(true);
        }
        t.start();
    }

    public void refresh() {
//        log.info("refresh : "+Thread.currentThread().getName());
        List<T> list = dataLoading();
        String json = GsonUtil.getGson().toJson(list);
        state = false;
        this.stringRedisTemplate.opsForValue().set(getKey(), json, refreshTime, TimeUnit.MINUTES);
        state = true;
    }

    public List<T> getData() {
        if (openCache == false) {
            return null;
        }
        int count = 0;
        while (true) {
            if (state == false) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                if (count >= 5) {
                    return null;
                }
            }else{
                String cacheResult = stringRedisTemplate.opsForValue().get(getKey());
                if (StringUtil.isNotEmpty(cacheResult)) {
                    return GsonUtil.fromJsonList(cacheResult, clazz);
                } else {
                    return null;
                }
            }
        }
    }

    protected String getKey() {
        return this.getClass().toString().replace("class ", "");
    }
}

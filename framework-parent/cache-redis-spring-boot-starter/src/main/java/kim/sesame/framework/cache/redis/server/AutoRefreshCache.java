package kim.sesame.framework.cache.redis.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 定时刷新缓存
 *
 * @param <T>
 */
@CommonsLog
public class AutoRefreshCache<T> implements InitializingBean {

    /**
     * redis 缓存操作
     */
    protected StringRedisTemplate stringRedisTemplate;
    /**
     * 定时刷新缓存的时间
     */
    protected int refreshTime;
    /**
     * 返回的list 里面对象的类型,主要是 gson 反序列化时会用到
     */
    protected Class clazz = Object.class;

    /**
     * 序列化时是否写空值进去
     */
    protected boolean isWriteNullValue = true;
    /**
     * 标识
     */
    protected volatile boolean state = false; //
    /**
     * 是否开启缓存, 如果 false  那么刷新任务不会开启, getData()方法也获取不到数据
     */
    protected boolean openCache = true;

    /**
     * 模板方法, 提供给子类重写
     *
     * @return
     */
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
        t.setDaemon(true);
//        if (Thread.currentThread().getName().equals("main")) {
//            t.setDaemon(true);
//        }
        t.start();
    }

    public void refresh() {
        String key = getKey();
        log.debug("-----------  自动刷新缓存数据  start  ----------------------------------------");
        log.debug(MessageFormat.format("存储时间 : {0} {1}, key : {2}", refreshTime, TimeUnit.MINUTES, key));
        List<T> list = dataLoading();
        String json = "";
        if (isWriteNullValue) {
            json = JSON.toJSONString(list, SerializerFeature.WriteMapNullValue);
        } else {
            json = JSON.toJSONString(list);
        }
        state = false;
        this.stringRedisTemplate.opsForValue().set(key, json, refreshTime, TimeUnit.MINUTES);
        state = true;
        log.debug("-----------  自动刷新缓存数据  end  ----------------------------------------");
    }

    /**
     * 对外提供的获取数据的方法
     *
     * @return
     */
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
            } else {
                String cacheResult = stringRedisTemplate.opsForValue().get(getKey());
                if (StringUtils.isNotEmpty(cacheResult)) {
                    return JSONArray.parseArray(cacheResult, clazz);
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

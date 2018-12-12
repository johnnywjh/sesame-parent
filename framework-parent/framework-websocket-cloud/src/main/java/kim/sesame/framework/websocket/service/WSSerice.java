package kim.sesame.framework.websocket.service;

import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import kim.sesame.framework.entity.GMap;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.context.SpringContextUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.websocket.Session;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@CommonsLog
public class WSSerice {

    public final static String WEBSOCKET_CONTROLLER_KEY = "kjxoienpxoi23pxlji,2l3i";// websocket 秘钥
    private final static String WS_REDIS_PREFIX = "ws_conn_"; //websocket 的连接
    private final static String WS_USER_ONLINE_KEY = "ws_user_online_list"; //websocket 的用户集合

    private static StringRedisTemplate stringRedisTemplate = null;
    private static RedisTemplate redisTemplate = null;

    private static String getWsRedisKey(String userKey) {
        return WS_REDIS_PREFIX + userKey;
    }

    private static StringRedisTemplate getStringRedisTemplate() {
        if (stringRedisTemplate == null) {
            stringRedisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);
        }
        return stringRedisTemplate;
    }

    private static RedisTemplate getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = (RedisTemplate) SpringContextUtil.getBean("redisTemplate");
        }
        return redisTemplate;
    }

    /**
     * 向连接池中添加连接
     *
     * @param userKey 用户编号,唯一
     * @param session ws session
     */
    public static void addConn(String userKey, Session session) {
        // 添加连接
        log.debug(MessageFormat.format("user create connect success , user : {0} , sessionId : {1}", userKey, session.getId()));
        WSConnPool.put(userKey, session);

        updateOnlineUser("sx", userKey);// 用户上线

        // 把连接信息保存到redis上去
        String redisKey = getWsRedisKey(userKey);
        String redisValue = SpringContextUtil.getCurrentIpPort();
        getStringRedisTemplate().opsForValue().set(redisKey, redisValue, 1, TimeUnit.HOURS);
        log.debug(MessageFormat.format("save ws to redis success , key : {0} , value : {1}", redisKey, redisValue));

        getRedisTemplate().opsForList().rightPush(WS_USER_ONLINE_KEY, userKey);
        getRedisTemplate().expire(WS_USER_ONLINE_KEY, 5, TimeUnit.HOURS);
    }

    /**
     * 移除连接
     * 3
     */
    public static void removeConn(String userKey) {
        log.debug(MessageFormat.format("user disconnect connect , user : {0}", userKey));
        WSConnPool.remove(userKey);

        updateOnlineUser("lx", userKey);// 用户离线
        // 从redis中删除连接信息
        String redisKey = getWsRedisKey(userKey);
        getStringRedisTemplate().delete(redisKey);

        getRedisTemplate().opsForList().remove(WS_USER_ONLINE_KEY, 0, userKey);
    }

    //1
    public static void removeConn(Session session) {
        Session s = null;
        String userKey = null;
        for (String key : WSConnPool.keySet()) {
            s = WSConnPool.getSession(key);
            if (session.getId().equals(s.getId())) {
                userKey = key;
                break;
            }
        }
        if (StringUtil.isNotEmpty(userKey)) {
            removeConn(userKey);
        }
    }


    /**
     * 获取所有的在线用户
     */
    public static List<String> getOnlineUser() {
        return getRedisTemplate().opsForList().range(WS_USER_ONLINE_KEY, 0, -1);
    }

    /**
     * 获取所有的在线用户的个数
     */
    public static Long getOnlineUserCount() {
        return getRedisTemplate().opsForList().size(WS_USER_ONLINE_KEY);
    }

    /**
     * 更新在线用户: sx(上线),lx(离线)
     */
    public static void updateOnlineUser(String method, String userKey) {
        GMap res = GMap.newMap();
        res.putAction("userKey", userKey);

        String json = getMsgString(method, res);

        sendMessageToUser(json, userKey);
    }


    /**
     * 向特定的用户发送数据
     *
     * @param userKey 用户标识
     * @param message 消息内容,处理过后的
     */
    private static void sendMessageToUser(String userKey, String message) {
        // 1 根据 userKey 查询和用户保存消息的连接
        String userConnIpPort = getStringRedisTemplate().opsForValue().get(getWsRedisKey(userKey));
        if (StringUtil.isNotEmpty(userConnIpPort)) {
            // 2 判断是不是当前应用
            if (userConnIpPort.equals(SpringContextUtil.getCurrentIpPort())) {
                // 2.1 是当前应用, 本地推送
                try {
                    log.debug("send message to user 1 : " + userKey + " ,message content : " + message);
                    Session session = WSConnPool.getSession(userKey);
                    if (session != null) {
                        session.getBasicRemote().sendText(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // 2.2 不是当前应用, 通过http接口推送
                String url = "http://" + userConnIpPort + "/wsUser/to";
                Map<String, String> map = new HashMap<>();
                map.put("wsKey", WEBSOCKET_CONTROLLER_KEY);
                map.put("userKey", userKey);
                map.put("message", message);
                String res = HttpRequest.post(url).form(map).body();
                log.debug("send message to user 2 : " + userKey + " ,message content : " + message);
                log.debug(res);
            }
        }

    }


    /**
     * 向特定的用户发送数据
     *
     * @param userKey    用户标识
     * @param methodName 前端js 方法名称
     * @param message    消息对象
     */
    public static void sendMessageToUser(String userKey, String methodName, Object message) {
        String json = getMsgString(methodName, message);
        sendMessageToUser(userKey, json);
    }

    /**
     * 向所有的用户发送消息
     *
     * @param userKey    发送人
     * @param methodName 前端js 方法名称
     * @param message    消息对象
     */
    /**
     * @param methodName  前端js 方法名称
     * @param message     消息对象
     * @param excludeUser 排除某些用户不发送
     */
    public static void sendMessageAll(String methodName, Object message, String[] excludeUser) {

        getOnlineUser().forEach(user -> {
            boolean isSend = !(StringUtil.equals(user, excludeUser));
            if (isSend) {
                sendMessageToUser(user, methodName, message);
            }
        });
    }

    public static String getMsgString(String methodName, Object params) {
        GMap gmap = GMap.newMap();
        gmap.putAction("method", methodName + "(" + JSON.toJSONString(params) + ")");

        return JSON.toJSONString(gmap);
    }


}

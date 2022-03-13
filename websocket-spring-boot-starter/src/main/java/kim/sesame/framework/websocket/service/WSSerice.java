package kim.sesame.framework.websocket.service;

import com.alibaba.fastjson.JSON;
import kim.sesame.common.utils.StringUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;

import javax.websocket.Session;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@CommonsLog
public class WSSerice {

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
    }

    /**
     * 移除连接
     * 3
     */
    public static void removeConn(String userKey) {
        log.debug(MessageFormat.format("user disconnect connect , user : {0}", userKey));
        WSConnPool.remove(userKey);

        updateOnlineUser("lx", userKey);// 用户离线
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
        if (StringUtils.isNotEmpty(userKey)) {
            removeConn(userKey);
        }
    }

    /**
     * 获取所有的在线用户
     */
    public static Set<String> getOnlineUser() {
        return WSConnPool.keySet();
    }

    /**
     * 获取所有的在线用户的个数
     */
    public static Integer getOnlineUserCount() {
        return WSConnPool.keySet().size();
    }

    /**
     * 更新在线用户: sx(上线),lx(离线)
     */
    public static void updateOnlineUser(String method, String userKey) {
        Map res = new HashMap();
        res.put("userKey", userKey);

        sendMessageAll(method, res, new String[]{userKey});
    }


    /**
     * 向特定的用户发送数据
     *
     * @param userKey 用户标识
     * @param message 消息内容,处理过后的
     */
    public static void sendMessageToUser(String userKey, String message) {
        try {
            log.debug("send message to user : " + userKey + " ,message content : " + message);
            Session session = WSConnPool.getSession(userKey);
            if (session != null) {
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
     * @param methodName  前端js 方法名称
     * @param message     消息对象
     * @param excludeUser 排除某些用户不发送
     */
    public static void sendMessageAll(String methodName, Object message, String[] excludeUser) {
        try {
            Session session = null;
            Set<String> keySet = WSConnPool.keySet();
            for (String key : keySet) {
                boolean isSend = !(StringUtil.equals(key, excludeUser));
                if (isSend) {
                    session = WSConnPool.getSession(key);
                    if (session != null) {
                        String json = getMsgString(methodName, message);
                        session.getBasicRemote().sendText(json);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMsgString(String methodName, Object params) {
        Map gmap = new HashMap();
        gmap.put("method", methodName + "(" + JSON.toJSONString(params) + ")");

        return JSON.toJSONString(gmap);
    }
}

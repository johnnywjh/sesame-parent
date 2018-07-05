package kim.sesame.framework.websocket.service;

import com.alibaba.fastjson.JSON;
import kim.sesame.framework.entity.GMap;
import lombok.extern.apachecommons.CommonsLog;

import javax.websocket.Session;
import java.io.IOException;
import java.text.MessageFormat;
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
    }

    //1
    public static void removeConn(Session session) {
        Session s = null;
        for (String key : WSConnPool.keySet()) {
            s = WSConnPool.getSession(key);
            if (session.getId().equals(s.getId())) {
                removeConnByUserKey(key);
                break;
            }
        }
    }

    //2
    private static void removeConnByUserKey(String userKey) {
        removeConn(userKey);
        updateOnlineUser("lx", userKey);// 用户离线
    }

    /**
     * 获取所有的在线用户
     */
    public static Set<String> getOnlineUser() {
        return WSConnPool.keySet();
    }

    /**
     * 更新在线用户: sx(上线),lx(离线)
     */
    public static void updateOnlineUser(String method, String userKey) {
        GMap res = GMap.newMap();
        res.putAction("userKey", userKey);

        String json = getMsgString(method, res);


        sendMessage(json, userKey);
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
     * 向所有的用户发送消息
     *
     * @param userKey    用户标识
     * @param methodName 前端js 方法名称
     * @param message    消息对象
     */
    public static void sendMessageAll(String userKey, String methodName, Object message) {
        try {
            Session session = null;
            Set<String> keySet = WSConnPool.keySet();
            for (String key : keySet) {
                session = WSConnPool.getSession(key);
                if (session != null) {
                    String json = getMsgString(methodName, message);
                    session.getBasicRemote().sendText(json);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage(String message, String name) {
        try {
            Session session = null;
            Set<String> keySet = WSConnPool.keySet();
            for (String key : keySet) {
                session = WSConnPool.getSession(key);
                if (session != null) {
                    if (!key.equals(name)) {
                        session.getBasicRemote().sendText(message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMsgString(String methodName, Object params) {
        GMap gmap = GMap.newMap();
        gmap.putAction("method", methodName + "(" + JSON.toJSONString(params) + ")");

        return JSON.toJSONString(gmap);
    }


}

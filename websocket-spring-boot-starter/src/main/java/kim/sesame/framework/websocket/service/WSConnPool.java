package kim.sesame.framework.websocket.service;


import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * webSocket连接池
 */
@Slf4j
public class WSConnPool {

    // 保存用户的连接
    private static Map<String, Session> connections = null;

    static {
        connections = new HashMap<>();
    }


    public static Session getSession(String key) {
        return WSConnPool.connections.get(key);
    }

    public static void put(String key, Session session) {
        WSConnPool.connections.put(key, session);
    }

    public static void remove(String key) {
        WSConnPool.connections.remove(key);
    }

    public static Set<String> keySet() {
        return WSConnPool.connections.keySet();
    }


}

package kim.sesame.framework.websocket.service;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.MessageFormat;

@CommonsLog
@Component
@ServerEndpoint("/websocket")
public class WebSocketService {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        log.info(MessageFormat.format("客户端连接成功 sessionId : {0} , 等待建立连接...", session.getId()));
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if ("heartBeatCheck".equals(message)) {
            // 是心跳消息
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 不是心跳消息,接受用户标识,保存用户信息
            WSSerice.addConn(message, session);
            log.info(MessageFormat.format("已成功建立连接,user_key", message));
        }
    }

    @OnClose
    public void onClose(Session session) {
        WSSerice.removeConn(session);
    }

    @OnError
    public void onError(Session s, Throwable t) {
        log.info(t.getMessage() + "session:" + s.getId());
    }
}

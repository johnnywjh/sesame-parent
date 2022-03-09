package kim.sesame.framework.websocket.controller;

import kim.sesame.framework.exception.BizException;
import kim.sesame.framework.response.Response;
import kim.sesame.framework.web.annotation.IgnoreLoginCheck;
import kim.sesame.framework.web.controller.AbstractWebController;
import kim.sesame.framework.websocket.service.WSConnPool;
import kim.sesame.framework.websocket.service.WSSerice;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;
import java.io.IOException;


@CommonsLog
@RestController
@RequestMapping("/wsUser")
public class WsUserController extends AbstractWebController {

    @IgnoreLoginCheck
    @RequestMapping("/to")
    public Response to(String wsKey, String userKey, String message) {
        if (WSSerice.WEBSOCKET_CONTROLLER_KEY.equals(wsKey) == false) {
            throw new BizException("方法验证失败");
        }
        try {
            log.debug("send message to user http : " + userKey + " ,message content : " + message);
            Session session = WSConnPool.getSession(userKey);
            if (session != null) {
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnSuccess();
    }
}

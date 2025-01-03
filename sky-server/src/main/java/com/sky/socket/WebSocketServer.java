package com.sky.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2025/1/3 23:46
 */
@Component
@Slf4j
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    private Map<String, Session> sessions = new HashMap();

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        log.info("onOpen: {}",sid);
        sessions.put(session.getId(), session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("onMessage: {}",message);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("onClose: {}",session.getId());
        sessions.remove(session.getId());
    }

    public void sendAllMessage(String message) {
        log.info(message);
        for (Session session : sessions.values()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

package com.maindark.livestream.service;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/webSocket")
@Slf4j
public class WebSocket {

    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);
        log.info("[webSocket] get a new connection ");
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        log.info("[webSocket] connect close" );
    }

    @OnMessage
    public void onMessage(String message){
        log.info("[webSocket] get a new message");
    }

    public void sendMessage(String message){
        for (WebSocket webSocket:webSocketSet ) {
            log.info("[webSocket] notice message={}",message);
            try {
                webSocket.session.getBasicRemote().sendText(message);
            }catch (Exception e){
                log.error("[webSocket] error:{}",e.getMessage());
            }



        }
    }
}

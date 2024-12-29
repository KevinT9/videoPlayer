package com.dlk.videoplayer.websocket.service;

import com.dlk.videoplayer.websocket.SessionStorage;
import com.dlk.videoplayer.websocket.handler.VideoWebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.WebSocketSession;

import java.net.URI;

@Service
public class WebSocketService {

    private final WebSocketClient webSocketClient;
    private final VideoWebSocketHandler videoWebSocketHandler;
    private final SessionStorage sessionStorage;

    public WebSocketService(VideoWebSocketHandler videoWebSocketHandler, SessionStorage sessionStorage) {
        this.videoWebSocketHandler = videoWebSocketHandler;
        this.sessionStorage = sessionStorage;
        this.webSocketClient = new StandardWebSocketClient();
    }

    // Metodo para crear una sesión WebSocket desde el servidor
    public WebSocketSession createSession(String sessionId) throws Exception {
        URI uri = URI.create("wss://loves-sharp-hall-bridge.trycloudflare.com/ws/video");  // La URL de tu WebSocket
        WebSocketSession session = webSocketClient.execute(videoWebSocketHandler, String.valueOf(uri)).get();
        sessionStorage.addSession(sessionId, session);  // Guardamos la sesión en el almacenamiento
        return session;
    }
}
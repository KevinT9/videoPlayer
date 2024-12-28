package com.dlk.videoplayer.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VideoWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private SessionStorage sessionStorage;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Conexión establecida: " + session.getId());
        sessionStorage.addSession(session.getId(), session);
        logSessions();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionStorage.removeSession(session.getId());
        System.out.println("Conexión cerrada: " + session.getId());
        logSessions();
    }

    public void sendVideoUrl(String videoUrl) throws Exception {
        if (sessionStorage.getSessions().isEmpty()) {
            System.out.println("No hay sesiones activas.");
            return;
        }

        for (WebSocketSession session : sessionStorage.getSessions().values()) {
            System.out.println("Sesión activa: " + session.getId());
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(videoUrl));
                System.out.println("Enviando video a la sesión " + session.getId() + ": " + videoUrl);
            } else {
                System.out.println("La sesión " + session.getId() + " no está abierta.");
            }
        }
    }

    private void logSessions() {
        System.out.println("Sesiones actuales:");
        for (String sessionId : sessionStorage.getSessions().keySet()) {
            System.out.println(" - " + sessionId);
        }
    }
}
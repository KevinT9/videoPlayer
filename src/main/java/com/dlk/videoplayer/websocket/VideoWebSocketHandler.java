package com.dlk.videoplayer.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VideoWebSocketHandler extends TextWebSocketHandler {

    private SessionStorage sessionStorage;

    public VideoWebSocketHandler(SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Conexión establecida: {}", session.getId());
        sessionStorage.addSession(session.getId(), session);
        logSessions();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionStorage.removeSession(session.getId());
        log.info("Código de cierre: {}", status.getCode());
        log.info("Conexión cerrada: {}", session.getId());
        logSessions();
    }

    public void sendVideoUrl(String videoUrl) throws Exception {
        if (sessionStorage.getSessions().isEmpty()) {
            log.info("No hay sesiones activas.");
            return;
        }

        for (WebSocketSession session : sessionStorage.getSessions().values()) {
            log.info("Sesión activa: {}", session.getId());
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(videoUrl));
                log.info("Enviando video a la sesión {}: {}", session.getId(), videoUrl);
            } else {
                log.info("La sesión {} no está abierta.", session.getId());
            }
        }
    }

    private void logSessions() {
        log.info("Sesiones actuales:");
        for (String sessionId : sessionStorage.getSessions().keySet()) {
            log.info(" - {}", sessionId);
        }
    }
}
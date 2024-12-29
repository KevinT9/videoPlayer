package com.dlk.videoplayer.websocket;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VideoWebSocketHandler extends TextWebSocketHandler {

    private final SessionStorage sessionStorage;

    public VideoWebSocketHandler(SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Conexión establecida: {}", session.getId());
//        sessionStorage.addSession(session.getId(), session);
        logSessions();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionStorage.removeSession(session.getId());
        log.info("Código de cierre: {}", status.getCode());
        log.info("Conexión cerrada: {}", session.getId());
        logSessions();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("Mensaje recibido de la sesión {}: {}", session.getId(), message.getPayload());
        // Recibir el mensaje del cliente
        String payload = message.getPayload();
        // Parsear el mensaje
        JSONObject jsonMessage = new JSONObject(payload);

        if (jsonMessage.getString("type").equals("registerSession")) {
            // Obtener el sessionId y registrarlo
            String sessionId = jsonMessage.getString("sessionId");
            sessionStorage.addSession(sessionId, session);  // Almacenar la sesión con el sessionId

            log.info("Sesión registrada con ID: {}", sessionId);
        }
        // Manejar otros tipos de mensaje...
    }

    public void sendVideoUrl(String videoUrl) throws Exception {
        if (findActiveSession()) return;

        for (WebSocketSession session : sessionStorage.getSessions().values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(videoUrl));
                log.info("Enviando video a la sesión por defecto {}: {}", session.getId(), videoUrl);
            } else {
                log.info("La sesión por defecto {} no está abierta.", session.getId());
            }
        }
    }

    // Metodo para enviar un video a una sesión en específico
    public void sendVideoUrlToSession(String videoUrl, String sessionId) throws Exception {
        if (findActiveSession()) return;

        for (WebSocketSession session : sessionStorage.getSessions().values()) {
            if (session.isOpen() && session.getId().equals(sessionId)) {
                session.sendMessage(new TextMessage(videoUrl));
                log.info("Enviando video a la sesión {}: {}", session.getId(), videoUrl);
            } else {
                log.info("La sesión {} no está abierta.", session.getId());
            }
        }
    }

    private boolean findActiveSession() {
        if (sessionStorage.getSessions().isEmpty()) {
            log.info("No hay sesiones activas.");
            return true;
        }
        return false;
    }

    // Metodo para registrar las sesiones activas
    private void logSessions() {
        log.info("Sesiones actuales:");
        for (String sessionId : sessionStorage.getSessions().keySet()) {
            log.info(" - {}", sessionId);
        }
    }
}
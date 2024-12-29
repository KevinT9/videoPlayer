package com.dlk.videoplayer.websocket.handler;

import com.dlk.videoplayer.model.dto.VideoSession;
import com.dlk.videoplayer.websocket.SessionStorage;
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
        logSessions();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String clave = sessionStorage.getKey(session);

        sessionStorage.removeSession(clave);
        log.info("Código de cierre: {}", status.getCode());
        log.info("Conexión cerrada clave: {}, valor: {}", clave, session.getId());
        logSessions();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
//        log.info("Mensaje recibido de la sesión {}: {}", session.getId(), message.getPayload());
        // Recibir el mensaje del cliente
        String payload = message.getPayload();
        // Parsear el mensaje
        JSONObject jsonMessage = new JSONObject(payload);

        if (jsonMessage.getString("type").equals("registerSession")) {
            // Obtener el sessionId y registrarlo
            String sessionId = jsonMessage.getString("sessionId");
            sessionStorage.addSession(sessionId, session);

            log.info("Sesión registrada con ID: {}", sessionId);
        }


        if (jsonMessage.getString("type").equals("videoState")) {
            // Obtener el videoUrl, currentTime y isPlaying
            String claveSesion = jsonMessage.getString("sessionId");
            String videoUrl = jsonMessage.getString("videoUrl");
            double currentTime = jsonMessage.getDouble("currentTime");
            boolean isPlaying = jsonMessage.getBoolean("isPlaying");

            // Crear un objeto VideoSession y actualizar su estado
            VideoSession videoSession = new VideoSession();
            videoSession.updateState(videoUrl, currentTime, isPlaying);

            WebSocketSession sessionW = sessionStorage.getSession(claveSesion);

            if (sessionW.isOpen()) {
                try {
                    sessionW.sendMessage(new TextMessage("{ \"type\": \"videoInfo\", \"videoUrl\": \""
                            + videoSession.getVideoUrl() + "\", \"currentTime\": "
                            + videoSession.getCurrentTime() + " }"));
//                    log.info("Actualizando el estado de {}: {}", sessionW.getId(), videoUrl);
                } catch (Exception e) {
                    log.error("Error al enviar el video a la sesión {}: {}", sessionW.getId(), e.getMessage());
                }
            } else {
                log.info("La sesión {} no está abierta, no se actualizo el estado", sessionW.getId());
            }


        }

    }

    // Metodo para enviar un video a una sesión en específico
    public void sendVideoUrlToSession(String videoUrl, String sessionId) throws Exception {
        if (findActiveSession()) return;

        for (WebSocketSession session : sessionStorage.getSessions().values()) {
            if (session.isOpen() && session.getId().equals(sessionId)) {
//                session.sendMessage(new TextMessage(videoUrl));

                VideoSession videoSession = new VideoSession();
                videoSession.updateState(videoUrl, 0, false);


                session.sendMessage(new TextMessage("{ \"type\": \"videoInfo\", \"videoUrl\": \""
                        + videoSession.getVideoUrl() + "\", \"currentTime\": "
                        + videoSession.getCurrentTime() + " }"));
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
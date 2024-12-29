package com.dlk.videoplayer.websocket;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Getter
@Component
public class SessionStorage {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void addSession(String sessionId, WebSocketSession session) {
        sessions.put(sessionId, session);
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    // Buscar por la clave del Map y retornar el valor
    public WebSocketSession getSession(String clave) {
        return sessions.get(clave);
    }

    // Buscar por el valor del Map y retornar la clave
    public String getKey(WebSocketSession valor) {
        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            if (entry.getValue().equals(valor)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
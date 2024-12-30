package com.dlk.videoplayer.websocket;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Getter
@Component
public class SessionStorage {

    private final Map<String, String> sessions = new ConcurrentHashMap<>();

    public void addSession(String sessionId, String alias) {
        sessions.put(sessionId, alias);
    }

}
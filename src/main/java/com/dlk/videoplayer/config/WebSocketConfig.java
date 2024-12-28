package com.dlk.videoplayer.config;

import com.dlk.videoplayer.websocket.VideoWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /**
     * Registrando WebSocket handler
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/ws/video").setAllowedOrigins("*");
    }

    /**
     * Creando un WebSocket handler bean
     */
    @Bean
    public WebSocketHandler myHandler() {
        return new VideoWebSocketHandler();
    }
}
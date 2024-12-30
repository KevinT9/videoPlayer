package com.dlk.videoplayer.websocket.config;

import com.dlk.videoplayer.Constantes;
import com.dlk.videoplayer.websocket.SessionStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Configura un broker en memoria para gestionar mensajes a través de canales
        registry.enableSimpleBroker("/topic", "/queue"); // Esto gestiona los mensajes a los canales
        registry.setApplicationDestinationPrefixes("/app"); // Prefijo para los mensajes que envían los clientes
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Configura el endpoint WebSocket
        registry.addEndpoint("/ws/video")
                .setAllowedOrigins(Constantes.URL_DEVELOPMENT, Constantes.URL_PRODUCTION)
                .withSockJS(); // Habilita SockJS para clientes sin WebSocket nativo
    }

    @Bean
    public SessionStorage sessionStorage() {
        return new SessionStorage();
    }
}
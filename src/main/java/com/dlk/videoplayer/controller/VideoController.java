package com.dlk.videoplayer.controller;

import com.dlk.videoplayer.model.dto.MensajeDTO;
import com.dlk.videoplayer.websocket.SessionStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
@Slf4j
public class VideoController {

    private final SimpMessagingTemplate messagingTemplate;
    private final SessionStorage sessionStorage;

    @Autowired
    public VideoController(SimpMessagingTemplate messagingTemplate, SessionStorage sessionStorage) {
        this.messagingTemplate = messagingTemplate;
        this.sessionStorage = sessionStorage;
    }

    // Maneja la conexión de un cliente WebSocket
    @MessageMapping("/connect")
    public void connectToSession(@Payload MensajeDTO mensajeDTO, SimpMessageHeaderAccessor headerAccessor) {
        log.info("Cliente conectado a la sesión: {}", mensajeDTO);

        // Asocia el cliente con el sessionId recibido
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("sessionId", mensajeDTO.getSessionId());

        switch (mensajeDTO.getType()) {
            case "registerSession" -> {
                log.info("Registrando sesión: {}", mensajeDTO.getSessionId());
                sessionStorage.addSession(mensajeDTO.getSessionId(), mensajeDTO.getUsername());

                messagingTemplate.convertAndSend("/topic/" + mensajeDTO.getSessionId(), mensajeDTO);
            }
            case "videoPlay" -> {
                log.info("Reproduciendo video: {}", mensajeDTO.getSessionId());

                messagingTemplate.convertAndSend("/topic/" + mensajeDTO.getSessionId(), mensajeDTO);
            }
            case "videoPause" -> {
                log.info("Pausando video: {}", mensajeDTO.getSessionId());

                messagingTemplate.convertAndSend("/topic/" + mensajeDTO.getSessionId(), mensajeDTO);
            }
            case "videoState" -> messagingTemplate.convertAndSend("/topic/" + mensajeDTO.getSessionId(), mensajeDTO);
            case "videoLoad" -> {
                log.info("Cargando video: {}", mensajeDTO.getSessionId());

                messagingTemplate.convertAndSend("/topic/" + mensajeDTO.getSessionId(), mensajeDTO);
            }
            case "chatMessage" -> {
                log.info("Mensaje de chat: {}", mensajeDTO);
                messagingTemplate.convertAndSend("/topic/" + mensajeDTO.getSessionId(), mensajeDTO);
            }
        }
    }

    // Enviar un mensaje a todos los clientes de una sesión
    @MessageMapping("/send")
    @SendTo("/topic/{sessionId}")
    public ResponseEntity<MensajeDTO> sendMessageToSession(@DestinationVariable String sessionId, @Payload MensajeDTO message) {
        // Reenvía el mensaje a todos los clientes en el canal de esa sesión
        log.info("Mensaje enviado a la sesión {}: {}", sessionId, message);
        return ResponseEntity.ok(message);
    }

}

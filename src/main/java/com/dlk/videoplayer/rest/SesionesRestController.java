package com.dlk.videoplayer.rest;

import com.dlk.videoplayer.model.dto.SesionesDTO;
import com.dlk.videoplayer.websocket.SessionStorage;
import com.dlk.videoplayer.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sesiones")
@Slf4j
public class SesionesRestController {

    private final SessionStorage sessionStorage;
    private final WebSocketService webSocketService;

    public SesionesRestController(SessionStorage sessionStorage, WebSocketService webSocketService) {
        this.sessionStorage = sessionStorage;
        this.webSocketService = webSocketService;
    }

    @GetMapping
    ResponseEntity<List<SesionesDTO>> getSesiones() {
        log.info("Solicitando lista de sesiones");
        // Listar las sesiones
        List<SesionesDTO> listaSesiones = new ArrayList<>();

        for (String sessionId : sessionStorage.getSessions().keySet()) {
            SesionesDTO sesionesDTO = new SesionesDTO();
            sesionesDTO.setNombre(sessionId);
            sesionesDTO.setSessionId(sessionId);

            listaSesiones.add(sesionesDTO);
        }

        return ResponseEntity.ok(listaSesiones);
    }

    @PostMapping("/join")
    ResponseEntity<Void> joinSession(@RequestBody SesionesDTO sesionesDTO) {
        log.info("Unirse a la sesión");


        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    ResponseEntity<SesionesDTO> createSession(@RequestBody SesionesDTO sesionesDTO) {
        log.info("Crear sesión");
        try {
            // Generar un ID único para la sesión

            String nombreSesion = sesionesDTO.getNombre();
            // Crear la sesión WebSocket desde el servidor
            WebSocketSession sesionCreada = webSocketService.createSession(nombreSesion);
            sesionesDTO.setSessionId(sesionCreada.getId());

            return ResponseEntity.ok(sesionesDTO);

        } catch (Exception e) {
            log.error("Error al crear la sesión: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}

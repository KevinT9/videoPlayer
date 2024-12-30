package com.dlk.videoplayer.rest;

import com.dlk.videoplayer.model.dto.SesionesDTO;
import com.dlk.videoplayer.websocket.SessionStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sesiones")
@Slf4j
public class SesionesRestController {

    private final SessionStorage sessionStorage;

    public SesionesRestController(SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
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
}
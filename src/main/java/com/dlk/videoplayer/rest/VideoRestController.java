package com.dlk.videoplayer.rest;

import com.dlk.videoplayer.model.dto.VideoListItem;
import com.dlk.videoplayer.websocket.SessionStorage;
import com.dlk.videoplayer.websocket.handler.VideoWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.dlk.videoplayer.Constantes.VIDEO_DIR;

@RestController
@Slf4j
public class VideoRestController {

    private final VideoWebSocketHandler videoWebSocketHandler;
    private final SessionStorage sessionStorage;

    public VideoRestController(VideoWebSocketHandler videoWebSocketHandler, SessionStorage sessionStorage) {
        this.videoWebSocketHandler = videoWebSocketHandler;
        this.sessionStorage = sessionStorage;
    }

    // Endpoint para recibir el nombre del video desde Postman
    @PostMapping("/play/{sessionId}")
    public ResponseEntity<?> playSong(@RequestBody VideoListItem videoListItem, @PathVariable String sessionId) {
        log.info("Recibiendo solicitud para reproducir: {} en la sessionID {}", videoListItem.filename(), sessionId);

        if (videoListItem.filename() == null || videoListItem.filename().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: El nombre de la canción no puede estar vacío");
        }

        // Crear la URL del video
        String videoUrl = "/videos/" + videoListItem.filename();

        try {
            for (String nombreSesion : sessionStorage.getSessions().keySet()) {
                if (nombreSesion.equals(sessionId)) {
                    String id = sessionStorage.getSession(sessionId).getId();
                    // Usar WebSocket para enviar la URL del video al frontend
                    videoWebSocketHandler.sendVideoUrlToSession(videoUrl, id);
                }
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al enviar el video: " + e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    // Endpoint para servir el video
    @GetMapping("/videos/{filename}")
    public ResponseEntity<Resource> getVideo(@PathVariable String filename) throws Exception {
        log.info("Solicitando video: {}", filename);
        Path videoPath = Paths.get(VIDEO_DIR).resolve(filename).normalize();
        Resource video = new UrlResource(videoPath.toUri());

        if (video.exists() && video.isReadable()) {
            return ResponseEntity.ok().body(video);
        } else {
            throw new Exception("Video no encontrado");
        }
    }
}
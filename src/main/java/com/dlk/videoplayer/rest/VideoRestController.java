package com.dlk.videoplayer.rest;

import com.dlk.videoplayer.model.dto.VideoListItem;
import com.dlk.videoplayer.websocket.VideoWebSocketHandler;
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
//    private final String VIDEO_DIR = "src/main/resources/videos/";

    public VideoRestController(VideoWebSocketHandler videoWebSocketHandler) {
        this.videoWebSocketHandler = videoWebSocketHandler;
    }

    // Endpoint para recibir el nombre del video desde Postman
    @PostMapping("/play")
    public ResponseEntity<?> playSong(@RequestBody VideoListItem videoListItem) {
        log.info("Recibiendo solicitud para reproducir: {}", videoListItem.filename());

        if (videoListItem.filename() == null || videoListItem.filename().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: El nombre de la canción no puede estar vacío");
        }

        // Crear la URL del video
        String videoUrl = "/videos/" + videoListItem.filename();

        try {
            // Usar WebSocket para enviar la URL del video al frontend
            videoWebSocketHandler.sendVideoUrl(videoUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al enviar el video: " + e.getMessage());
        }

        return ResponseEntity.ok().build();  // Confirma que el video se envió
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
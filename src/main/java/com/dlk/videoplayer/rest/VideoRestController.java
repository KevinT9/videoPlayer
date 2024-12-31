package com.dlk.videoplayer.rest;

import com.dlk.videoplayer.Util.VideoListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.dlk.videoplayer.Constantes.VIDEO_DIR;

@RestController
@Slf4j
public class VideoRestController {

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

    @GetMapping("/api/videos")
    public List<String> getVideoList() {
        log.info("Solicitando lista de videos");
        return VideoListUtil.getVideoList();
    }


}
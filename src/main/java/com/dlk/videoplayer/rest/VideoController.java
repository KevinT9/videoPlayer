package com.dlk.videoplayer.rest;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.dlk.videoplayer.Constantes.VIDEO_DIR;

@RestController
public class VideoController {

    @GetMapping("/videos/{filename}")
    public ResponseEntity<Resource> getVideo(@PathVariable String filename) throws Exception {
        Path videoPath = Paths.get(VIDEO_DIR).resolve(filename).normalize();
        Resource video = new UrlResource(videoPath.toUri());

        if (video.exists() && video.isReadable()) {
            return ResponseEntity.ok().body(video);
        } else {
            throw new Exception("Video no encontrado");
        }
    }

    @GetMapping("/play/{songName}")
    public String playSong(@PathVariable String songName) {
        return "/videos/" + songName;  // Ruta donde el frontend buscará el video
    }
}
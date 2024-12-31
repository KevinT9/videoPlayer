package com.dlk.videoplayer.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoSession {
    private String videoUrl;
    private double currentTime; // Posición actual de reproducción
    private boolean isPlaying;  // Estado de reproducción (true si está reproduciendo, false si está pausado)
    private String nameVideo;
    private String afterVideo;
    private String beforeVideo;
}
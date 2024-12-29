package com.dlk.videoplayer.model.dto;

import lombok.Getter;

public class VideoSession {
    @Getter
    private String videoUrl;
    @Getter
    private double currentTime; // Posición actual de reproducción
    private boolean isPlaying;  // Estado de reproducción (true si está reproduciendo, false si está pausado)

    // Métodos para actualizar y obtener la información del video
    public void updateState(String videoUrl, double currentTime, boolean isPlaying) {
        this.videoUrl = videoUrl;
        this.currentTime = currentTime;
        this.isPlaying = isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
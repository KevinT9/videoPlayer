package com.dlk.videoplayer.model.dto;

import lombok.Data;

@Data
public class MensajeDTO {
    private String type;
    private String sessionId;
    private String message;
    private String username;
    private VideoSession video;
}

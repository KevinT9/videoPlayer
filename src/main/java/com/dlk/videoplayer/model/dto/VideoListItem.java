package com.dlk.videoplayer.model.dto;


public class VideoListItem {
    private String filename;

    public VideoListItem(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}

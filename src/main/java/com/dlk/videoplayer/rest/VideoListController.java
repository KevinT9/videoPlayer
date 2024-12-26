package com.dlk.videoplayer.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.dlk.videoplayer.Constantes.VIDEO_DIR;

@RestController
public class VideoListController {

    @GetMapping("/api/videos")
    public List<String> getVideoList() {
        File folder = new File(VIDEO_DIR);
        File[] files = folder.listFiles();
        List<String> videoNames = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".mp4")) {
                    videoNames.add(file.getName());
                }
            }
        }

        return videoNames;
    }
}
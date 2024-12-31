package com.dlk.videoplayer.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.dlk.videoplayer.Constantes.VIDEO_DIR;

public class VideoListUtil {

    public static List<String> getVideoList() {
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

    public static String getNextVideo(String currentVideo) {
        List<String> videoList = getVideoList();
        int currentIndex = videoList.indexOf(currentVideo);
        if (currentIndex != -1 && currentIndex < videoList.size() - 1) {
            return videoList.get(currentIndex + 1);
        } else {
            return null;
        }
    }

    public static String getPreviousVideo(String currentVideo) {
        List<String> videoList = getVideoList();
        int currentIndex = videoList.indexOf(currentVideo);
        if (currentIndex > 0) {
            return videoList.get(currentIndex - 1);
        } else {
            return null;
        }
    }

    // Modificar la URL del video "/videos/nombre" -> "/videos/newVideoName"
    public static String modifyVideoNameUrl(String url, String newVideoName) {
        return url.substring(0, url.lastIndexOf("/") + 1) + newVideoName;
    }
}

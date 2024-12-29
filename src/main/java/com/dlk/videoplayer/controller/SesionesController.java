package com.dlk.videoplayer.controller;

import com.dlk.videoplayer.model.dto.SesionesDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sesiones")
public class SesionesController {

    @GetMapping
    String getSesiones() {
        return "inicio";
    }

    @RequestMapping("/join/{sessionId}")
    String joinSession(@PathVariable String sessionId, Model model) {
        SesionesDTO sesionesDTO = new SesionesDTO();
        sesionesDTO.setSessionId(sessionId);
        sesionesDTO.setNombre(sessionId);
        model.addAttribute("sesion", sesionesDTO);
        return "playlistVideoSesion";
    }


    @RequestMapping("/play/{sessionId}")
    String playSession(@PathVariable String sessionId, Model model) {
        SesionesDTO sesionesDTO = new SesionesDTO();
        sesionesDTO.setSessionId(sessionId);
        sesionesDTO.setNombre(sessionId);
        model.addAttribute("sesion", sesionesDTO);
        return "videoPlayerSesion";
    }
}

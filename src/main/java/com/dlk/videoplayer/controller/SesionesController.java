package com.dlk.videoplayer.controller;

import com.dlk.videoplayer.model.dto.MensajeDTO;
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

    @RequestMapping("/lista/{sessionId}")
    String obtenerListaSesiones(@PathVariable String sessionId, Model model) {
        MensajeDTO mensajeDTO = new MensajeDTO();
        mensajeDTO.setSessionId(sessionId);
        model.addAttribute("sesion", mensajeDTO);
        return "playlistVideoMessage";
    }

    @RequestMapping("/play/{username}/{sessionId}")
    String playSession(@PathVariable String username, @PathVariable String sessionId, Model model) {
        SesionesDTO sesionesDTO = new SesionesDTO();
        sesionesDTO.setSessionId(sessionId);
        sesionesDTO.setNombre(username);
        model.addAttribute("sesion", sesionesDTO);
        return "videoMessage";
    }

    @RequestMapping("/play/visitante/{sessionId}")
    String playSessionV(@PathVariable String sessionId, Model model) {
        SesionesDTO sesionesDTO = new SesionesDTO();
        sesionesDTO.setSessionId(sessionId);
        sesionesDTO.setNombre("");
        model.addAttribute("sesion", sesionesDTO);
        return "videoMessage";
    }
}

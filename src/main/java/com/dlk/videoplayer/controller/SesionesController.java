package com.dlk.videoplayer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sesiones")
public class SesionesController {

    @GetMapping
    String getSesiones() {
        return "inicio";
    }
}

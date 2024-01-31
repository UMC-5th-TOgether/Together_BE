package com.backend.together.domain.healthcheck.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HealthController {
    @GetMapping("/test") //헬스체크용 api
    public String healthCheck(){
        return "I'm healthy";
    }
}

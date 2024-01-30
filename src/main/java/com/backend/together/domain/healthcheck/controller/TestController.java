package com.backend.together.domain.healthcheck.controller;

import com.backend.together.domain.block.dto.BlockRequestDTO;
import com.backend.together.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/test") //헬스체크용 api
    public String healthCheck(){
        return "I'm healthy";
    }
}

package com.backend.together.domain.post.controller;


import com.backend.together.domain.post.dto.TestRequestBodyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class BaseController {

    @GetMapping
    public String testController(){
        return "Hello World! 강채원";
    }
}

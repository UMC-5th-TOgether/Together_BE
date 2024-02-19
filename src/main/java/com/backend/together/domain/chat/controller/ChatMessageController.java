package com.backend.together.domain.chat.controller;

import com.backend.together.domain.chat.dto.ApiRes;
import com.backend.together.domain.chat.dto.SocketMessageResponseDto;
import com.backend.together.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @GetMapping("/message/{chatRoomId}")
    public ApiRes<List<SocketMessageResponseDto>> getMessages(@PathVariable Long chatRoomId) {
        return chatMessageService.getMessages(chatRoomId);
    }
}

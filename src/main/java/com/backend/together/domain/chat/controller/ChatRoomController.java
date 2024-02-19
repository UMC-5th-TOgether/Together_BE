package com.backend.together.domain.chat.controller;

import com.backend.together.domain.chat.dto.ChatRoomDto;
import com.backend.together.domain.chat.dto.ChatRoomInfoDto;
import com.backend.together.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    //내가 속한 채팅방 목록 조회
    @GetMapping("/rooms/me")
    public List<ChatRoomDto> getMyChatRooms() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        return chatRoomService.findChatRoomsByUserId(userId);
    }

    //특정 채팅방 정보 조회
    @GetMapping("/rooms/{chatRoomId}")
    public ChatRoomInfoDto getChatRoomInfo(@PathVariable Long chatRoomId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        return chatRoomService.findChatRoomInfo(chatRoomId,userId);
    }
}
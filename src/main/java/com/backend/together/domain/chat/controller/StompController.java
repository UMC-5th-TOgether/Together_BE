package com.backend.together.domain.chat.controller;

import com.backend.together.domain.chat.dto.SocketMessageRequestDto;
import com.backend.together.domain.chat.dto.SocketMessageResponseDto;
import com.backend.together.domain.chat.entity.ReadMessagePayload;
import com.backend.together.domain.chat.entity.SocketMessage;
import com.backend.together.domain.chat.service.ChatMessageService;
import com.backend.together.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j(topic = "Message")
public class StompController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

//    @MessageMapping("/message")
//    public void receiveMessage(@Payload SocketMessageRequestDto socketMessageRequestDto){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long memberId = Long.parseLong(authentication.getName());
//
//        log.info("receiveMessage " + socketMessageRequestDto.getMessage());
//        Long chatRoomId = socketMessageRequestDto.getChatRoomId();
//        SocketMessage socketMessage = chatMessageService.saveMessage(socketMessageRequestDto);
//        log.info("receiveMessage " + socketMessage.getSender());
//        SocketMessageResponseDto chatMessage = SocketMessageResponseDto.builder()
//                .chatRoomId(chatRoomId)
//                .sender(socketMessage.getSender())
//                .time((socketMessage.getTime()))
//                .message(socketMessage.getMessage())
//                .build();
//        simpMessageSendingOperations.convertAndSend("/topic/" + chatRoomId + "/message", chatMessage);
//    }
@MessageMapping("/message")
public void receiveMessage(@Payload SocketMessageRequestDto socketMessageRequestDto){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
        Long memberId = Long.parseLong(authentication.getName());

        log.info("receiveMessage " + socketMessageRequestDto.getMessage());
        Long chatRoomId = socketMessageRequestDto.getChatRoomId();
        SocketMessage socketMessage = chatMessageService.saveMessage(socketMessageRequestDto);
        log.info("receiveMessage " + socketMessage.getSender());
        SocketMessageResponseDto chatMessage = SocketMessageResponseDto.builder()
                .chatRoomId(chatRoomId)
                .sender(socketMessage.getSender())
                .time((socketMessage.getTime()))
                .message(socketMessage.getMessage())
                .build();
        simpMessageSendingOperations.convertAndSend("/topic/" + chatRoomId + "/message", chatMessage);
    } else {
        log.error("Authentication is null or not authenticated");
    }
}

    @MessageMapping("/readMessage")
    public void handleReadMessage(@Payload ReadMessagePayload payload) {
        Long messageId = payload.getMessageId();
        String sender = payload.getSender();
        chatMessageService.markMessageAsRead(messageId, sender);
    }
}

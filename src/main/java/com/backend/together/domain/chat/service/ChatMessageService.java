package com.backend.together.domain.chat.service;

import com.backend.together.domain.chat.dto.ApiRes;
import com.backend.together.domain.chat.dto.SocketMessageRequestDto;
import com.backend.together.domain.chat.dto.SocketMessageResponseDto;
import com.backend.together.domain.chat.entity.ChatRoom;
import com.backend.together.domain.chat.entity.SocketMessage;
import com.backend.together.domain.chat.repository.ChatMessageRepository;
import com.backend.together.domain.chat.repository.ChatRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "Chat Message Service")
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository,
                              ChatRoomRepository chatRoomRepository,
                              SimpMessageSendingOperations simpMessageSendingOperations) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    public SocketMessage saveMessage(SocketMessageRequestDto socketMessageRequestDto) {
//        Claims userInfoFromToken = jwtUtil.getUserInfoFromToken(socketMessageRequestDto.getToken());
//        String username = userInfoFromToken.getSubject();
        ZonedDateTime time = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        Long chatRoomId = socketMessageRequestDto.getChatRoomId();

        // ChatRoom 엔터티 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new Error("ChatRoom not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        SocketMessage socketMessage = SocketMessage.builder()
                .chatRoom(chatRoom)
                .sender(username)
                .time(time)
                .message(socketMessageRequestDto.getMessage())
                .build();

        chatMessageRepository.save(socketMessage);

        // WebSocket을 통해 메시지 전송
        SocketMessageResponseDto chatMessage = SocketMessageResponseDto.builder()
                .chatRoomId(chatRoomId)
                .sender(socketMessage.getSender())
                .time(socketMessage.getTime())
                .message(socketMessage.getMessage())
                .build();

        simpMessageSendingOperations.convertAndSend("/topic/" + chatRoomId + "/message", chatMessage);

        return socketMessage;
    }

    @Transactional
    public void markMessageAsRead(Long messageId, String senderUsername) {
        SocketMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getSender().equals(senderUsername)) {
            message.setIsRead(true);
        }

        List<SocketMessage> olderMessages = chatMessageRepository
                .findAllByChatRoomIdAndSenderAndIsReadFalseAndTimeBefore(
                        message.getChatRoom().getId(),
                        message.getSender(),
                        message.getTime()
                );

        for (SocketMessage olderMessage : olderMessages) {
            olderMessage.setIsRead(true);
        }

        chatMessageRepository.save(message);
        chatMessageRepository.saveAll(olderMessages);
    }


    public ApiRes<List<SocketMessageResponseDto>> getMessages(Long chatRoomId) {
        List<SocketMessage> socketMessageList = chatMessageRepository.findAllByChatRoomId(chatRoomId);

        List<SocketMessageResponseDto> socketMessageResponseDtoList = new ArrayList<>();

        for (SocketMessage socketMessage : socketMessageList) {
            socketMessageResponseDtoList.add(SocketMessageResponseDto.builder()
                    .chatRoomId(socketMessage.getChatRoom().getId())
                    .sender(socketMessage.getSender())
                    .messageId(socketMessage.getId())
                    .message(socketMessage.getMessage())
                    .time(socketMessage.getTime())
                    .build()
            );
        }
        return ApiRes.successData(socketMessageResponseDtoList);
    }
}
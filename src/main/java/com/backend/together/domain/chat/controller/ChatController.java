//package com.backend.together.domain.chat.controller;
//
//import com.backend.together.domain.chat.dto.*;
//import com.backend.together.domain.chat.service.ChatService;
//import com.backend.together.domain.member.entity.MemberEntity;
//import com.backend.together.domain.member.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Queue;
//
//@RestController
//@RequiredArgsConstructor
//public class ChatController {
//    private final ChatService chatService;
//    private final SimpMessagingTemplate msgOperation;
//    private final MemberRepository memberRepository;
//
//    @GetMapping("/chatRoom/list")
//    public ResponseEntity<Queue<ChatRoomInfoResponseDto>> findAllChatRoom() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long memberId = Long.parseLong(authentication.getName());
//
//        return chatService.findAllChatRoom(memberId);
//    }
//
//    @GetMapping("/chatRoom/enter/{receiverId}")
//    public ResponseEntity<ChatRoomResponseDto> enterChatRoom(@PathVariable Long receiverId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        MemberEntity userOne = memberRepository.findById(Long.parseLong(authentication.getName()))
//                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
//
//        return chatService.enterChatRoom(userOne, receiverId);
//    }
//
//    @MessageMapping("/chat/message")
//    public void enterChatRoom(ChatRequestDto chatRequestDto) {
//        ChatResponseDto chatResponseDto = chatService.saveChat(chatRequestDto);
//        ChatDetailResponseDto chatDetailResponseDto = chatService.createChatDetailResponseDto(chatResponseDto);
//        msgOperation.convertAndSend("/sub/chat/room/" + chatRequestDto.getChatRoomId(), chatResponseDto);
//    }
//
//    @MessageMapping("/chat/read")
//    public void readChat(ChatResponseDto chatResponseDto) {
//        chatService.markAsRead(chatResponseDto);
//
//    }
//    @GetMapping("/chat/unread")
//    public ResponseEntity<Boolean> existUnReadChat(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long memberId = Long.parseLong(authentication.getName());
//
//        return chatService.checkUnReadChat(memberId);
//    }
//}

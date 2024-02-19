package com.backend.together.domain.chat.controller;

import com.backend.together.domain.chat.dto.ApiRes;
import com.backend.together.domain.chat.dto.ChatRoomDto;
import com.backend.together.domain.chat.dto.ChatRoomInfoDto;
import com.backend.together.domain.chat.dto.ChatRoomResponseDto;
import com.backend.together.domain.chat.service.ChatRoomService;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final MemberRepository memberRepository;

    //내가 속한 채팅방 목록 조회
//    @GetMapping("/rooms/me")
//    public List<ChatRoomDto> getMyChatRooms() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.parseLong(authentication.getName());
//        return chatRoomService.findChatRoomsByUserId(userId);
//    }

    @GetMapping("/rooms/me")
    public List<ChatRoomDto> getMyChatRooms(Authentication authentication) {
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
    @PostMapping("/rooms/create")
    public ResponseEntity<ApiRes<ChatRoomResponseDto>> createChatRoom(@RequestParam Long sentUserId, @RequestParam Long receivedUserId) {
        // 사용자 정보 검색
        MemberEntity sentUser = memberRepository.findById(sentUserId).orElse(null);
        MemberEntity receivedUser = memberRepository.findById(receivedUserId).orElse(null);

        // 채팅방 생성
        ApiRes<ChatRoomResponseDto> response = chatRoomService.createRoom(sentUser, receivedUser);

        // 응답에 따라 상태 코드 설정
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

}

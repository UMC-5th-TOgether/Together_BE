//package com.backend.together.domain.chat.dto;
//
//import com.backend.together.domain.member.entity.MemberEntity;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//import java.time.LocalDateTime;
//
//@Getter
//@AllArgsConstructor
//public class ChatDetailResponseDto {
//    private String senderUserName;
//    private String message;
//    private LocalDateTime createdAt;
//    private Long senderId;
//
//    public static ChatDetailResponseDto from(ChatResponseDto chat, MemberEntity member){
//        return new ChatDetailResponseDto(member.getNickname(),
////                users.getProfileImg(),
//                chat.getMessage(),
//                chat.getCreatedAt(),
//                member.getMemberId());
//    }
//}
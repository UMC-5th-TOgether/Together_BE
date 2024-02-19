package com.backend.together.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class ChatRoomDto {
    private Long chatRoomId;
    private List<String> users;
    private Long userId;
//    private String nickname;
//    private String image;
    private Long latestMessageId;
    private String latestMessage;
    private String latestMessageTime;
}

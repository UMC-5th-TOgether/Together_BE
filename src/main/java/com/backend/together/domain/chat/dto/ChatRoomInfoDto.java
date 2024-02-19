package com.backend.together.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ChatRoomInfoDto {
    private Long userId;
    private String name;
//    private String nickname;
//    private String image;
    private List<SocketMessageResponseDto> messages;
}

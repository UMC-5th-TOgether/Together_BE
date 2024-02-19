package com.backend.together.domain.chat.dto;

import com.backend.together.domain.chat.entity.Status;
import lombok.Getter;

@Getter
public class SocketMessageRequestDto {
    private Status status;
    private Long chatRoomId;
    private String message;
    private String token;

}

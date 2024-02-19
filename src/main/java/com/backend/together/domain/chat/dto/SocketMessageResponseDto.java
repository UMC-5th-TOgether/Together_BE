package com.backend.together.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class SocketMessageResponseDto {
    private Long chatRoomId;
    private Long messageId;
    private String sender;
    private String message;
    private ZonedDateTime time;
}

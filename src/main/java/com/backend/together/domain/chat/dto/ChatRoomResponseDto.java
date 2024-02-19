package com.backend.together.domain.chat.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//import java.util.List;
//
//@Getter
//@AllArgsConstructor

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomResponseDto {
    private Long chatRoomId;
}
//    private Long chatRoomId;
//    private List<ChatResponseDto> chatList;
//    private Long receiverId;
//    private String receiverName;
//}

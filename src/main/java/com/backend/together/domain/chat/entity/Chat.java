//package com.backend.together.domain.chat.entity;
//
//import jakarta.persistence.Id;
//import lombok.Data;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//
//@Data
//@Document(collection = "chat")
//public class Chat {
//    @Id
//    private String id;
//    private String uuid;
//    private String message;
//    private Long chatRoomId;
//    private Long senderId;
//    private Long receiverId;
//    private LocalDateTime createdAt;
//    private Boolean readStatus = false;
//
//    private Chat(String message, Long chatRoomId, Long senderId ,Long receiverId, LocalDateTime createdAt) {
//        this.message = message;
//        this.chatRoomId = chatRoomId;
//        this.senderId = senderId;
//        this.receiverId = receiverId;
//        this.createdAt = createdAt;
//        this.uuid = UUID.randomUUID().toString().substring(0,8);
//    }
//
//    public Chat() {
//
//    }
//
//    public static Chat of(String message, Long chatRoomId, Long senderId, Long receiverId, LocalDateTime createdAt){
//        return new Chat(message,chatRoomId,senderId,receiverId,createdAt);
//    }
//
//    public void updateReadStatus(){
//        this.readStatus = true;
//    }
//}
//

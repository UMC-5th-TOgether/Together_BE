//package com.backend.together.domain.chat.repository;
//
//import com.backend.together.domain.chat.entity.Chat;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.repository.query.Param;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//public interface ChatRepository extends MongoRepository<Chat,String> {
//    List<Chat> findByChatRoomIdAndCreatedAtAfter(Long ChatRoomId,LocalDateTime date);
//    Optional<Chat> findTopByChatRoomIdAndCreatedAtAfterOrderByCreatedAtDesc(Long chatRoomId, LocalDateTime createdAt);
//    Boolean existsByReceiverIdAndReadStatus(Long userId,Boolean readStatus);
//}
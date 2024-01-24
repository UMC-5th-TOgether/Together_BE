package com.backend.together.domain.chat.repository;

import com.backend.together.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, String> {
    List<Chat> findByChatRoomIdAndCreatedAtAfter(Long chatRoomId, LocalDateTime date);
    Optional<Chat> findTopByChatRoomIdAndCreatedAtAfterOrderByCreatedAtDesc(Long chatRoomId, LocalDateTime createdAt);
    Boolean existsByReceiverIdAndReadStatus(Long memberID, Boolean readStatus);

    @Modifying
    @Query("UPDATE Chat c SET c.readStatus = true WHERE c.chatRoomId = :chatRoomId AND c.receiverId = :receiverId AND c.readStatus = false")
    void updateChatReadStatus(@Param("chatRoomId") Long chatRoomId, @Param("receiverId") Long receiverId);
    @Modifying
    @Query("UPDATE Chat c SET c.createdAt = :createdAt WHERE c.chatRoomId = :chatRoomId")
    void saveChat(@Param("chatRoomId") Long chatRoomId, @Param("createdAt") LocalDateTime createdAt);

    List<Chat> findByChatRoomId(Long chatRoomId);
}

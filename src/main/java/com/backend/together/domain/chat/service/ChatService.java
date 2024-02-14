package com.backend.together.domain.chat.service;

import com.backend.together.domain.chat.dto.*;
import com.backend.together.domain.chat.entity.Chat;
import com.backend.together.domain.chat.entity.ChatRoom;
import com.backend.together.domain.chat.repository.ChatRepository;
import com.backend.together.domain.chat.repository.ChatRoomRepository;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {


    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final MongoTemplate mongoTemplate;

    public ResponseEntity<Queue<ChatRoomInfoResponseDto>> findAllChatRoom(Long memberId) {
        Queue<ChatRoomInfoResponseDto> chatRoomInfoResponseDtoQueue = new PriorityQueue<>(Comparator.comparing(ChatRoomInfoResponseDto::getLastChatTime));
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByHostOrGuest(member, member);


        for (ChatRoom chatRoom : chatRoomList) {
            Optional<Chat> chatOptional = chatRepository.findTopByChatRoomIdAndCreatedAtAfterOrderByCreatedAtDesc(chatRoom.getId(), chatRoom.getHostEntryTime());

            if (chatOptional.isPresent()) {
                Chat chat = chatOptional.get();
                boolean isCurrentUserHost = chatRoom.getHost().getMemberId().equals(memberId);

                boolean isUnread = chat.getReceiverId().equals(memberId) && !chat.getReadStatus();

                ChatRoomInfoResponseDto chatRoomInfoResponseDto = new ChatRoomInfoResponseDto(
                        chatRoom.getId(),
                        chat.getCreatedAt(),
                        ChatResponseDto.from(chat),
//                        isCurrentUserHost ? chatRoom.getGuest().getProfileImg() : chatRoom.getHost().getProfileImg(),
                        isCurrentUserHost ? chatRoom.getGuest().getMemberId() : chatRoom.getHost().getMemberId(),
                        isCurrentUserHost ? chatRoom.getGuest().getNickname() : chatRoom.getHost().getNickname(),
//                        isCurrentUserHost ? chatRoom.getGuest().getRole() : chatRoom.getHost().getRole(),
                        isUnread
                );

                chatRoomInfoResponseDtoQueue.add(chatRoomInfoResponseDto);
            }
        }

        return ResponseEntity.ok(chatRoomInfoResponseDtoQueue);
    }

//    public ResponseEntity<ChatRoomResponseDto> enterChatRoom(MemberEntity userOne, Long userTwoId) {
//        MemberEntity userTwo = memberRepository.findById(userTwoId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
//        Optional<ChatRoom> chatRoom = chatRoomRepository.findChatRoomByHostAndGuest(userOne, userTwo);
//        ChatRoomResponseDto chatRoomResponseDto;
//
//        if (chatRoom.isPresent()) {
//            ChatRoom findChatRoom = chatRoom.get();
//
//            chatRepository.saveChat(findChatRoom.getId(), LocalDateTime.now());
//
//            List<Chat> chatList;
//            chatRepository.updateChatReadStatus(findChatRoom.getId(), userTwo.getMemberId());
//
//            if (findChatRoom.getGuest().getMemberId().equals(userOne)) {
//                chatList = chatRepository.findByChatRoomIdAndCreatedAtAfter(findChatRoom.getId(), findChatRoom.getGuestEntryTime());
//            } else {
//                chatList = chatRepository.findByChatRoomIdAndCreatedAtAfter(findChatRoom.getId(), findChatRoom.getHostEntryTime());
//            }
//
//            List<ChatResponseDto> chatListDto = chatList.stream().map(ChatResponseDto::from).collect(Collectors.toList());
//            chatListDto.sort(Comparator.comparing(ChatResponseDto::getCreatedAt));
//
//            chatRoomResponseDto = new ChatRoomResponseDto(findChatRoom.getId(),
//                    chatListDto,
////                    userTwo.getProfileImg(),
//                    userTwoId,
//                    userTwo.getNickname());
//            return ResponseEntity.ok(chatRoomResponseDto);
//        } else {
//            Long chatRoomId = createChatRoom(userOne.getMemberId(), userTwoId);
//            chatRoomResponseDto = new ChatRoomResponseDto(chatRoomId, new ArrayList<>(), userTwoId, userTwo.getNickname());
//            return ResponseEntity.ok(chatRoomResponseDto);
//        }
//    }

    public ResponseEntity<ChatRoomResponseDto> enterChatRoom(MemberEntity userOne , Long userTwoId) {
        MemberEntity userTwo = memberRepository.findById(userTwoId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        Optional<ChatRoom> chatRoom = chatRoomRepository.findChatRoomByUsers(userOne.getMemberId(), userTwoId);
        ChatRoomResponseDto chatRoomResponseDto;
        if (chatRoom.isPresent()) {
            ChatRoom findChatRoom = chatRoom.get();
            //redisService.saveChatsToDB(findChatRoom.getId());
            List<Chat> chatList;
            Query query = new Query();
            query.addCriteria(new Criteria().andOperator(
                    Criteria.where("chatRoomId").is(findChatRoom.getId()),
                    Criteria.where("senderId").is(userTwo.getMemberId()),
                    Criteria.where("readStatus").is(false)
            ));
            Update update = Update.update("readStatus", true);
            mongoTemplate.updateMulti(query, update, Chat.class);
            if (findChatRoom.getGuest().getMemberId().equals(userOne.getMemberId())) {
                chatList = chatRepository.findByChatRoomIdAndCreatedAtAfter(findChatRoom.getId(),findChatRoom.getGuestEntryTime());
            } else {
                chatList = chatRepository.findByChatRoomIdAndCreatedAtAfter(findChatRoom.getId(), findChatRoom.getHostEntryTime());
            }
            List<ChatResponseDto> chatListDto = chatList.stream().map(ChatResponseDto::from).collect(Collectors.toList());
            chatListDto.sort(new Comparator<ChatResponseDto>() {
                @Override
                public int compare(ChatResponseDto o1, ChatResponseDto o2) {
                    return o1.getCreatedAt().compareTo(o2.getCreatedAt());
                }
            });
            chatRoomResponseDto = new ChatRoomResponseDto(findChatRoom.getId(),
                    chatListDto,
                    //userTwo.getImage(),
                    userTwoId,
                    userTwo.getNickname());
            return ResponseEntity.ok(chatRoomResponseDto);
        } else {
            Long chatRoomId = createChatRoom(userOne.getMemberId(), userTwoId);
            chatRoomResponseDto = new ChatRoomResponseDto(chatRoomId,new ArrayList<>(), userTwoId, userTwo.getNickname());
            return ResponseEntity.ok(chatRoomResponseDto);
        }
    }

    public Long createChatRoom(Long hostId, Long guestId) {
        MemberEntity userOne = memberRepository.findById(hostId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        MemberEntity userTwo = memberRepository.findById(guestId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        ChatRoom chatRoom = ChatRoom.of(userOne, userTwo, LocalDateTime.now().plusHours(9));
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return savedChatRoom.getId();
    }

    public ChatResponseDto saveChat(ChatRequestDto chatRequestDto) {
        Chat chat = ChatRequestDto.toEntity(chatRequestDto, LocalDateTime.now().plusHours(9));
        chatRepository.save(chat);
        return ChatResponseDto.from(chat);
    }

    public ChatDetailResponseDto createChatDetailResponseDto(ChatResponseDto chatResponseDto) {
        MemberEntity sender = memberRepository.findById(chatResponseDto.getSenderId()).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        return ChatDetailResponseDto.from(chatResponseDto, sender);
    }
//    @Autowired
//    public ChatService(MemberRepository memberRepository, ChatRoomRepository chatRoomRepository, ChatRepository chatRepository) {
//        this.memberRepository = memberRepository;
//        this.chatRoomRepository = chatRoomRepository;
//        this.chatRepository = chatRepository;
//        this.mongoTemplate = mongo
//    }

    public ResponseEntity<String> markAsRead(ChatResponseDto chatResponseDto) {
        // Chat 객체를 DB에서 가져오거나 다른 저장소에서 가져와서 업데이트
        Optional<Chat> optionalChat = chatRepository.findById(chatResponseDto.getUuid());

        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            chat.updateReadStatus();
            chatRepository.save(chat); // 또는 업데이트할 저장소에 따라 다른 메서드 사용
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chat not found");
        }
    }

    public ResponseEntity<Boolean> checkUnReadChat(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        List<ChatRoom> findAllChatRoom = chatRoomRepository.findAllByHostOrGuest(member,member);
        boolean haveToRead = chatRepository.existsByReceiverIdAndReadStatus(memberId, false);

        return ResponseEntity.ok(haveToRead);
    }

}


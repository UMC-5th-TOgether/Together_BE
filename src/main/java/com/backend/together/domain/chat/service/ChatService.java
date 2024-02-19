//package com.backend.together.domain.chat.service;
//
//import com.backend.together.domain.chat.dto.*;
//import com.backend.together.domain.chat.entity.Chat;
//import com.backend.together.domain.chat.entity.ChatRoom;
//import com.backend.together.domain.chat.repository.ChatRepository;
//import com.backend.together.domain.chat.repository.ChatRoomRepository;
//import com.backend.together.domain.member.entity.MemberEntity;
//import com.backend.together.domain.member.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class ChatService {
//
//
//    private final ChatRoomRepository chatRoomRepository;
//    private final ChatRepository chatRepository;
//    private final MemberRepository memberRepository;
//    private final MongoTemplate mongoTemplate;
//    private final RedisService redisService;
//
//
//
//    public ResponseEntity<Queue<ChatRoomInfoResponseDto>> findAllChatRoom(Long userOne, Long userTwo, Long memberId) {
//        Queue<ChatRoomInfoResponseDto> chatRoomInfoResponseDtoQueue = new PriorityQueue<>(Comparator.comparing(ChatRoomInfoResponseDto::getLastChatTime).reversed());
//
//        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByHostOrGuest(userOne, userTwo);
//
//        for (ChatRoom chatRoom : chatRoomList) {
//            redisService.saveChatsToDB(chatRoom.getId());
//            ChatRoomInfoResponseDto chatRoomInfoResponseDto = createChatRoomInfoResponseDto(chatRoom, memberId);
//            if (chatRoomInfoResponseDto != null) {
//                chatRoomInfoResponseDtoQueue.add(chatRoomInfoResponseDto);
//            }
//        }
//
//        return ResponseEntity.ok(chatRoomInfoResponseDtoQueue);
//    }
//
//
//    private ChatRoomInfoResponseDto createChatRoomInfoResponseDto(ChatRoom chatRoom, Long memberId) {
//        Optional<Chat> chatOptional;
//        Chat chat;
//
//        if (chatRoom.getHost().getMemberId().equals(memberId)) {
//            chatOptional = chatRepository.findTopByChatRoomIdAndCreatedAtAfterOrderByCreatedAtDesc(chatRoom.getId(), chatRoom.getHostEntryTime());
//        } else {
//            chatOptional = chatRepository.findTopByChatRoomIdAndCreatedAtAfterOrderByCreatedAtDesc(chatRoom.getId(), chatRoom.getGuestEntryTime());
//        }
//
//        if (chatOptional.isPresent()) {
//            chat = chatOptional.get();
//            boolean unread = chat.getReceiverId().equals(memberId) && !chat.getReadStatus();
//
//            return new ChatRoomInfoResponseDto(chatRoom.getId(), chat.getCreatedAt(), ChatResponseDto.from(chat),
//                    chatRoom.getGuest().getMemberId(), chatRoom.getGuest().getNickname(), unread);
//        }
//
//        return null;
//    }
//
//    public ResponseEntity<ChatRoomResponseDto> enterChatRoom(MemberEntity userOne , Long userTwoId) {
//        MemberEntity userTwo = memberRepository.findById(userTwoId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
//        Optional<ChatRoom> chatRoom = chatRoomRepository.findChatRoomByUsers(userOne.getMemberId(), userTwoId);
//        ChatRoomResponseDto chatRoomResponseDto;
//        if (chatRoom.isPresent()) {
//            ChatRoom findChatRoom = chatRoom.get();
//            //redisService.saveChatsToDB(findChatRoom.getId());
//            List<Chat> chatList;
//            Query query = new Query();
//            query.addCriteria(new Criteria().andOperator(
//                    Criteria.where("chatRoomId").is(findChatRoom.getId()),
//                    Criteria.where("senderId").is(userTwo.getMemberId()),
//                    Criteria.where("readStatus").is(false)
//            ));
//            Update update = Update.update("readStatus", true);
//            mongoTemplate.updateMulti(query, update, Chat.class);
//            if (findChatRoom.getGuest().getMemberId().equals(userOne.getMemberId())) {
//                chatList = chatRepository.findByChatRoomIdAndCreatedAtAfter(findChatRoom.getId(),findChatRoom.getGuestEntryTime());
//            } else {
//                chatList = chatRepository.findByChatRoomIdAndCreatedAtAfter(findChatRoom.getId(), findChatRoom.getHostEntryTime());
//            }
//            List<ChatResponseDto> chatListDto = chatList.stream().map(ChatResponseDto::from).collect(Collectors.toList());
//            chatListDto.sort(new Comparator<ChatResponseDto>() {
//                @Override
//                public int compare(ChatResponseDto o1, ChatResponseDto o2) {
//                    return o1.getCreatedAt().compareTo(o2.getCreatedAt());
//                }
//            });
//            chatRoomResponseDto = new ChatRoomResponseDto(findChatRoom.getId(),
//                    chatListDto,
//                    //userTwo.getImage(),
//                    userTwoId,
//                    userTwo.getNickname());
//            return ResponseEntity.ok(chatRoomResponseDto);
//        } else {
//            Long chatRoomId = createChatRoom(userOne.getMemberId(), userTwoId);
//            chatRoomResponseDto = new ChatRoomResponseDto(chatRoomId,new ArrayList<>(), userTwoId, userTwo.getNickname());
//            return ResponseEntity.ok(chatRoomResponseDto);
//        }
//    }
//
//    public Long createChatRoom(Long hostId, Long guestId) {
//        MemberEntity userOne = memberRepository.findById(hostId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
//        MemberEntity userTwo = memberRepository.findById(guestId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
//        ChatRoom chatRoom = ChatRoom.of(userOne, userTwo, LocalDateTime.now().plusHours(9));
//        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
//        return savedChatRoom.getId();
//    }
//
//    public ChatResponseDto saveChat(ChatRequestDto chatRequestDto) {
//        Chat chat = ChatRequestDto.toEntity(chatRequestDto, LocalDateTime.now().plusHours(9));
//        redisService.setChatValues(chat, chat.getChatRoomId(),chat.getUuid());
//        if(redisService.getChat(chat.getChatRoomId(),chat.getUuid())==null) throw new Error("채팅 저장 오류");
//        return ChatResponseDto.from(chat);
//    }
//
//    public ChatDetailResponseDto createChatDetailResponseDto(ChatResponseDto chatResponseDto) {
//        MemberEntity sender = memberRepository.findById(chatResponseDto.getSenderId()).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
//        return ChatDetailResponseDto.from(chatResponseDto, sender);
//    }
//
//    public ResponseEntity<String> markAsRead(ChatResponseDto chatResponseDto){
//        Chat chat = redisService.getChat(chatResponseDto.getChatRoomId(), chatResponseDto.getUuid());
//        chat.updateReadStatus();
//        redisService.setChatValues(chat,chat.getChatRoomId(),chat.getUuid());
//        return ResponseEntity.ok("success");
//    }
//    public ResponseEntity<Boolean> checkUnReadChat(Long memberId){
//        List<ChatRoom> findAllChatRoom = chatRoomRepository.findAllByHostOrGuest(memberId, memberId);
//        for (ChatRoom chatRoom : findAllChatRoom) {
//            redisService.saveChatsToDB(chatRoom.getId());
//        }
//        Boolean haveToRead = chatRepository.existsByReceiverIdAndReadStatus(memberId, false);
//        return ResponseEntity.ok(haveToRead);
//    }
//    public ResponseEntity<String> saveChatList(Long chatRoomId){
//        redisService.saveChatsToDB(chatRoomId);
//        return ResponseEntity.ok("success");
//    }
//}
//

package com.backend.together.domain.post.controller;

import com.backend.together.domain.block.Entity.Block;
import com.backend.together.domain.block.service.BlockServiceImpl;
import com.backend.together.domain.category.Category;

import com.backend.together.domain.post.service.HashtagService;
import com.backend.together.domain.post.service.PostHashtagService;
import com.backend.together.global.apiPayload.ApiResponse;
import com.backend.together.global.enums.Gender;
import com.backend.together.global.enums.PostStatus;
import com.backend.together.domain.post.Post;
import com.backend.together.domain.post.dto.PostRequestDTO;
import com.backend.together.domain.post.dto.PostResponseDTO;
import com.backend.together.domain.post.dto.TestRequestBodyDTO;
import com.backend.together.domain.post.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import com.backend.together.domain.post.converter.StringToEnumConverterFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/*
* 추후 : @AuthenticationPrincipal
*  MemberId 설정
* ERROR 코드 만들기
* auth : 강채원 (2024.01.17)
* 수정 : 일자 & 내용
* */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostServiceImpl service;
    private final BlockServiceImpl blockService;
    private final PostHashtagService postHashtagService;
    StringToEnumConverterFactory factory = new StringToEnumConverterFactory();

    /*
    *
    * entities -> dtos -> responseDTO -> responseEntity
    * return post
    * */
    public void updateViewList(List<Post> list) {
        // update view
        Iterator<Post> iterator = list.iterator();

        while (iterator.hasNext()){
            Long id = iterator.next().getId();
            service.updateView(id);
        }

    }
    public void updateView(Post p) {
        // update view
        service.updateView(p.getId());
    }
    @GetMapping
    public ResponseEntity<?> findAllPost(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.parseLong(authentication.getName());
//
//        List<Post> entities = service.retrieve();
//        List<PostResponseDTO> dtos = new ArrayList<>();
//
//        for (Post post : entities) {
//
//            List<String> postHashtags = postHashtagService.getHashtagToStringByPost(post);
//
//            PostResponseDTO dto = new PostResponseDTO(post);
//            dto.setPostHashtagList(postHashtags);
//
//            dtos.add(dto);
//        }
//
//<<<<<<< HEAD
//=======
//        //차단한 유저의 post 필터링
//        List<Block> blockedList = blockService.getBlockedMember(userId);
//        List<Post> filteredPosts = entities.stream()
//                .filter(post -> blockedList.stream()
//                        .noneMatch(blocked -> post.getMemberId().equals(blocked.getBlocked().getMemberId())))
//                .toList();
//
//        List<PostResponseDTO> dtos = filteredPosts.stream().map(PostResponseDTO::new).toList();
//
//>>>>>>> 1bddba26955310a40395bb41fd97ea858d067d6b
//        ApiResponse<List<PostResponseDTO>> response = ApiResponse.onSuccess(dtos);
//        return ResponseEntity.ok().body(response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        List<Block> blockedList = blockService.getBlockedMember(userId);

        List<PostResponseDTO> dtos = service.retrieve().stream()
                .filter(post -> blockedList.stream()
                        .noneMatch(blocked -> post.getMemberId().equals(blocked.getBlocked().getMemberId())))
                .map(post -> {
                    List<String> postHashtags = postHashtagService.getHashtagToStringByPost(post);
                    PostResponseDTO dto = new PostResponseDTO(post);
                    dto.setPostHashtagList(postHashtags);
                    return dto;
                })
                .toList();

        ApiResponse<List<PostResponseDTO>> response = ApiResponse.onSuccess(dtos);
        return ResponseEntity.ok().body(response);

    }
    @GetMapping("/id")
    public ResponseEntity<?> findById(@RequestParam Long postId){

        Post entity = service.retrievePostById(postId).get();
        updateView(entity);

        List<String> list = postHashtagService.getHashtagToStringByPost(entity);
        PostResponseDTO responseDTO = new PostResponseDTO(entity);
        responseDTO.setPostHashtagList(list);

//        Post post = entity.orElse(null);
//        if (post != null) {
//            updateView(post);
//        }
//
//        if(entity.isEmpty()) {
//            return ResponseEntity.badRequest().body(entity);
//        }
        ApiResponse<PostResponseDTO> response = ApiResponse.onSuccess(responseDTO);
        return ResponseEntity.ok().body(response);

    }

    /*
     * api : url/api/posts/keyword?keyword=22
     * entities -> dtos -> responseDTO -> responseEntity
     * return posts by memberId
     * */
    @GetMapping("/keyword")
    public ResponseEntity<?> findPostsByKeyword(@RequestParam String keyword) {
        List<Post> entities = service.retrievePostsByKeyword(keyword);

        List<PostResponseDTO> dtos = entities.stream().map(PostResponseDTO::new).collect(Collectors.toList());
        ApiResponse<List<PostResponseDTO>> response = ApiResponse.onSuccess(dtos);
        return ResponseEntity.ok().body(response);
    }
    /*
     * api : url/api/posts/member?memberId=22
     * entities -> dtos -> responseDTO -> responseEntity
     * return posts by memberId
     * */
    @GetMapping("/member")
    public ResponseEntity<?> findPostsByMember(@RequestParam Long memberId) {
        List<Post> entities = service.retrievePostByMemberId(memberId);
        List<PostResponseDTO> dtos = entities.stream().map(PostResponseDTO::new).collect(Collectors.toList());
        ApiResponse<List<PostResponseDTO>> response = ApiResponse.onSuccess(dtos);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/category")
    public ResponseEntity<?> findPostsByEnumCategory(@RequestParam Category category) {
        // 팩토리 인스턴스 생성

        // 컨버터 생성 (여기서는 Category Enum에 대한 컨버터 생성 예제)
        StringToEnumConverterFactory.StringToEnumConverter<Category> converter =
                (StringToEnumConverterFactory.StringToEnumConverter<Category>) factory.getConverter(Category.class);
        Category categoryEnum = converter.convert(String.valueOf(category));

        List<Post> entities = service.retrievePostsByCategory(categoryEnum);
        List<PostResponseDTO> dtos = entities.stream().map(PostResponseDTO::new).collect(Collectors.toList());
        ApiResponse<List<PostResponseDTO>> response = ApiResponse.onSuccess(dtos);
        return ResponseEntity.ok().body(response);

//        List<Post> entities = service.retrievePostsByCategory(category);

    }
    @GetMapping("/gender")
    public ResponseEntity<?> findPostsByEnumGender(@RequestParam Gender gender) {

        // 컨버터 생성 (여기서는 Category Enum에 대한 컨버터 생성 예제)
        StringToEnumConverterFactory.StringToEnumConverter<Gender> converter =
                (StringToEnumConverterFactory.StringToEnumConverter<Gender>) factory.getConverter(Gender.class);
        Gender genderEnum = converter.convert(String.valueOf(gender));

        List<Post> entities = service.retrievePostsByGender(genderEnum);
        List<PostResponseDTO> dtos = entities.stream().map(PostResponseDTO::new).collect(Collectors.toList());
        ApiResponse<List<PostResponseDTO>> response = ApiResponse.onSuccess(dtos);
        return ResponseEntity.ok().body(response);

    }
    @GetMapping("/status")
    public ResponseEntity<?> findPostsByEnumStatus(@RequestParam PostStatus status) {

        // 컨버터 생성 (여기서는 Category Enum에 대한 컨버터 생성 예제)
        StringToEnumConverterFactory.StringToEnumConverter<PostStatus> converter =
                (StringToEnumConverterFactory.StringToEnumConverter<PostStatus>) factory.getConverter(PostStatus.class);
        PostStatus statusEnum = converter.convert(String.valueOf(status));

        List<Post> entities = service.retrievePostsByStatus(statusEnum);

        List<PostResponseDTO> dtos = entities.stream().map(PostResponseDTO::new).collect(Collectors.toList());
        ApiResponse<List<PostResponseDTO>> response = ApiResponse.onSuccess(dtos);
        return ResponseEntity.ok().body(response);

    }

    /*
    * ? list로 반환? 1개 반환?
    * string -> enums
    *parameters : post data
    * dto -> entity -> save
    *return post with id
    * */
    @PostMapping // gender, memberid, category 해결해야함
    public ResponseEntity<?> createPost(@RequestBody PostRequestDTO requestDTO) {

        // 사용자 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(authentication.getName());

        Post newPost = PostRequestDTO.toEntity(requestDTO);
        // hashtag service에서 list return 해줌
        newPost.setMemberId(memberId);
        // service에서 확인해서 해시태그 잇으먄 그걸 반환. 없으면 새로 만들어서 넣어줌 ㅅ
        service.createPost(newPost);
// 이부분이 헷갈림 : createPost하면 참조값만 전달되는거??////////////////////////////////////
        postHashtagService.saveHashtag(newPost, requestDTO.getPostHashtagList());

        // 다시 조회하여 반환
        Post savedPost = service.retrievePostById(newPost.getId()).get(); // 예시로 findById 메서드를 사용했으며, 실제 사용하는 메서드에 따라 다를 수 있습니다.
        List<String> hashtagList = postHashtagService.getHashtagToStringByPost(savedPost);
        PostResponseDTO dto = new PostResponseDTO(savedPost);
        dto.setPostHashtagList(hashtagList);
        ApiResponse<PostResponseDTO> response = ApiResponse.onSuccess(dto);
        if(dto == null) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);

    }
    /*
    * /api/posts?postId={postId}
    *
    *
    **/
    @DeleteMapping
    public ResponseEntity<?> deletePostById(@RequestParam Long postId) {
        service.deletePost(postId);
        ApiResponse<?> response = ApiResponse.successWithoutResult();
        return ResponseEntity.ok().body(response);
    }

}

package com.backend.together.domain.post.controller;

import com.backend.together.domain.category.Category;
import com.backend.together.domain.enums.Gender;
import com.backend.together.domain.enums.PostStatus;
import com.backend.together.domain.post.Post;
import com.backend.together.domain.post.dto.PostRequestDTO;
import com.backend.together.domain.post.dto.PostResponseDTO;
import com.backend.together.domain.post.dto.TestRequestBodyDTO;
import com.backend.together.domain.post.service.PostService;
import com.backend.together.domain.post.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import com.backend.together.domain.post.converter.StringToEnumConverterFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private PostServiceImpl service;
    StringToEnumConverterFactory factory = new StringToEnumConverterFactory();

    @GetMapping("/testRequestBody")
    public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
        return "Hello World" + testRequestBodyDTO.toString() + "Message: " + testRequestBodyDTO.getMessage();
    }
    /*
    *
    * entities -> dtos -> responseDTO -> responseEntity
    * return post
    * */
    @GetMapping("/")
    public ResponseEntity<?> findAllPost(){
        List<Post> entities = service.retrieve();
        List<PostRequestDTO> dtos = entities.stream().map(PostRequestDTO::new).collect(Collectors.toList());
        PostResponseDTO<PostRequestDTO> response = PostResponseDTO.<PostRequestDTO>builder().
                data(dtos)
                .build();
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
        List<PostRequestDTO> dtos = entities.stream().map(PostRequestDTO::new).collect(Collectors.toList());
        PostResponseDTO<PostRequestDTO> response = PostResponseDTO.<PostRequestDTO>builder()
                .data(dtos)
                .build();
        return ResponseEntity.ok().body(response);
    }
    /*
     * api : url/api/posts/member?memberId=22
     * entities -> dtos -> responseDTO -> responseEntity
     * return posts by memberId
     * */
    @GetMapping("/member")
    public ResponseEntity<?> findPostsByKeyword(@RequestParam Long memberId) {
        List<Post> entities = service.retrievePostByMemberId(memberId);
        List<PostRequestDTO> dtos = entities.stream().map(PostRequestDTO::new).collect(Collectors.toList());
        PostResponseDTO<PostRequestDTO> response = PostResponseDTO.<PostRequestDTO>builder().
                data(dtos)
                .build();
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
        List<PostRequestDTO> dtos = entities.stream().map(PostRequestDTO::new).collect(Collectors.toList());
        PostResponseDTO<PostRequestDTO> response = PostResponseDTO.<PostRequestDTO>builder().
                data(dtos)
                .build();
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
        List<PostRequestDTO> dtos = entities.stream().map(PostRequestDTO::new).collect(Collectors.toList());
        PostResponseDTO<PostRequestDTO> response = PostResponseDTO.<PostRequestDTO>builder().
                data(dtos)
                .build();
        return ResponseEntity.ok().body(response);

    }
    @GetMapping("/status")
    public ResponseEntity<?> findPostsByEnumStatus(@RequestParam PostStatus status) {

        // 컨버터 생성 (여기서는 Category Enum에 대한 컨버터 생성 예제)
        StringToEnumConverterFactory.StringToEnumConverter<PostStatus> converter =
                (StringToEnumConverterFactory.StringToEnumConverter<PostStatus>) factory.getConverter(PostStatus.class);
        PostStatus statusEnum = converter.convert(String.valueOf(status));

        List<Post> entities = service.retrievePostsByStatus(statusEnum);

        List<PostRequestDTO> dtos = entities.stream().map(PostRequestDTO::new).collect(Collectors.toList());
        PostResponseDTO<PostRequestDTO> response = PostResponseDTO.<PostRequestDTO>builder().
                data(dtos)
                .build();
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
//        Post newPost = PostRequestDTO.toEntity(requestDTO);
//        service.createPost(newPost);
//        // 다시 조회하여 반환
//        Optional<Post> savedPost = service.retrievePostById(newPost.getId()); // 예시로 findById 메서드를 사용했으며, 실제 사용하는 메서드에 따라 다를 수 있습니다.
//        if(savedPost.isEmpty()) {
//            Optional<PostRequestDTO> requestDTO1 = savedPost.stream().findFirst().map(PostRequestDTO::new);
//
//            return ResponseEntity.badRequest().body(requestDTO1);
//        }
//        List<PostRequestDTO> dtos = savedPost.stream().map(PostRequestDTO::new).collect(Collectors.toList());
//        PostResponseDTO<PostRequestDTO> response = PostResponseDTO.<PostRequestDTO>builder().
//                data(dtos)
//                .build();
//        return ResponseEntity.ok().body(response);
        Post newPost = PostRequestDTO.toEntity(requestDTO);
        service.createPost(newPost);
        // 다시 조회하여 반환
        Optional<Post> savedPost = service.retrievePostById(newPost.getId()); // 예시로 findById 메서드를 사용했으며, 실제 사용하는 메서드에 따라 다를 수 있습니다.
        if(savedPost.isEmpty()) {
            return ResponseEntity.badRequest().body(savedPost);
        }
        return ResponseEntity.ok().body(savedPost);

    }
    /*
    * /api/posts?postId={postId}
    *
    *
    **/
    @DeleteMapping
    public ResponseEntity<?> deletePostById(@RequestParam Long postId) {
        service.deletePost(postId);
        return ResponseEntity.ok().body(postId);
    }

}

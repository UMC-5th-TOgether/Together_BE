package com.backend.together.domain.post.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.backend.together.domain.block.Entity.Block;
import com.backend.together.domain.block.service.BlockServiceImpl;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import com.backend.together.domain.member.service.MemberService;
import com.backend.together.domain.post.converter.PostMemberConverter;

import com.backend.together.global.apiPayload.code.status.ErrorStatus;
import com.backend.together.global.apiPayload.exception.handler.CustomHandler;

import com.backend.together.global.aws.s3.AmazonS3Manager;
import com.backend.together.global.aws.s3.Uuid;
import com.backend.together.global.enums.Category;

import com.backend.together.domain.post.service.PostHashtagService;
import com.backend.together.global.apiPayload.ApiResponse;
import com.backend.together.global.enums.Gender;
import com.backend.together.global.enums.PostStatus;
import com.backend.together.domain.post.Post;
import com.backend.together.domain.post.dto.PostRequestDTO;
import com.backend.together.domain.post.dto.PostResponseDTO;
import com.backend.together.domain.post.service.PostServiceImpl;
import com.backend.together.global.response.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.backend.together.domain.post.converter.StringToEnumConverterFactory;
import lombok.val;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
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
    private final AmazonS3Manager awsManager;

    private final PostServiceImpl service;
    private final BlockServiceImpl blockService;
    private final PostHashtagService postHashtagService;
    private final PostMemberConverter postMemberConverter;
    private final MemberRepository memberRepository;
    StringToEnumConverterFactory factory = new StringToEnumConverterFactory();

    @PostMapping("/upload")
    public ResponseEntity<ResponseDto> uploadPostImages(@RequestParam("post") String postId, @RequestPart("files") MultipartFile[] images) {
        try {
            boolean isSuccess = service.uploadImages(postId, images);

            ResponseDto<Boolean> responseDto = ResponseDto.<Boolean>builder()
                    .code(HttpStatus.OK.value())
                    .isSuccess(isSuccess)
                    .message("이미지 업로드 성공")
                    .data(Collections.singletonList(isSuccess))
                    .build();

            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .isSuccess(false)
                    .message("이미지 업로드 실패 - " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDto);
        }
    }


    // test...
//    @Autowired
//    private AmazonS3 amazonS3Client;
//    @RequestMapping("/upload")


//    @PostMapping("/multipart-files")
//    public ResponseEntity<String> uploadMultipleFiles(
//            @RequestParam("type") String type,
//            @RequestPart("uploadFiles") List<MultipartFile> multipartFiles) {
//
//        try {
//            for (MultipartFile file : multipartFiles) {
//                ObjectMetadata objectMetadata = new ObjectMetadata();
//                objectMetadata.setContentType(file.getContentType());
//                objectMetadata.setContentLength(file.getSize());
//
//                PutObjectRequest putObjectRequest = new PutObjectRequest(
//                        "bucketName",
//                        "objectKey",
//                        file.getInputStream(),
//                        objectMetadata);
//
//                amazonS3Client.putObject(putObjectRequest);
//            }
//            return ResponseEntity.ok("업로드 완료");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 중 오류 발생");
//        }
//    }
// ..................................................................

    @GetMapping()
    public ResponseEntity<?> findAllPost(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        List<Block> blockedList = blockService.getBlockedMember(userId);

        List<PostResponseDTO> dtos = service.retrieve().stream()
                .filter(post -> blockedList.stream()
                        .noneMatch(blocked -> post.getMember().getMemberId().equals(blocked.getBlocked().getMemberId())))
                .map(post -> {
                    List<String> postHashtags = postHashtagService.getHashtagToStringByPost(post);
                    PostResponseDTO responseDTO =PostResponseDTO.convertPostToDTO(post, post.getMember());

                    responseDTO.setPostHashtagList(postHashtags);
                    return responseDTO;
                })
                .toList();



        ApiResponse<List<PostResponseDTO>> response = ApiResponse.onSuccess(dtos);
        return ResponseEntity.ok().body(response);

    }
    @GetMapping("/{postId}")
    public ApiResponse<?> findById(@PathVariable(name = "postId") Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        MemberEntity member = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Post entity = service.retrievePostById(postId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.POST_NOT_FOUND));

        service.updateView(entity);

        List<String> list = postHashtagService.getHashtagToStringByPost(entity);
        MemberEntity writer = entity.getMember();

        PostResponseDTO.PostResponseDTO2 responseDTO = PostResponseDTO.PostResponseDTO2.responseDTO2(entity, writer, member);
        responseDTO.setPostHashtagList(list);

        return ApiResponse.onSuccess(responseDTO);
    }

    /*
     * api : url/api/posts/keyword?keyword=22
     * entities -> dtos -> responseDTO -> responseEntity
     * return posts by memberId
     * */
    @GetMapping(params = "keyword")
    public ResponseEntity<?> findPostsByKeyword(@RequestParam String keyword) {
        List<Post> entities = service.retrievePostsByKeyword(keyword);
        List<PostResponseDTO> dtos = entities.stream()
                .map(post -> {
                    List<String> hashtagList = postHashtagService.getHashtagToStringByPost(post);
                    PostResponseDTO responseDTO = PostResponseDTO.convertPostToDTO(post, post.getMember());
                    responseDTO.setPostHashtagList(hashtagList);
                    return responseDTO;
                })
                .collect(Collectors.toList());


//        List<PostResponseDTO> dtos = entities.stream().map(PostResponseDTO::new).collect(Collectors.toList());
        ApiResponse<List<PostResponseDTO>> response = ApiResponse.onSuccess(dtos);
        return ResponseEntity.ok().body(response);
    }
    /*
     * api : url/api/posts/member?memberId=22
     * entities -> dtos -> responseDTO -> responseEntity
     * return posts by memberId
     * */
    @GetMapping(params = "memberId")
    public ResponseEntity<?> findPostsByMember(@RequestParam Long memberId) {
        List<Post> entities = service.retrievePostByMemberId(memberId);
        List<PostResponseDTO> dtos = entities.stream()
                .map(post -> PostResponseDTO.convertPostToDTO(post, post.getMember()))
                .collect(Collectors.toList());
        ApiResponse<List<PostResponseDTO>> response = ApiResponse.onSuccess(dtos);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping(params = "category")
    public ResponseEntity<?> findPostsByEnumCategory(@RequestParam Category category) {
        // 팩토리 인스턴스 생성

        // 컨버터 생성 (여기서는 Category Enum에 대한 컨버터 생성 예제)
        StringToEnumConverterFactory.StringToEnumConverter<Category> converter =
                (StringToEnumConverterFactory.StringToEnumConverter<Category>) factory.getConverter(Category.class);
        Category categoryEnum = converter.convert(String.valueOf(category));

        List<Post> entities = service.retrievePostsByCategory(categoryEnum);
        List<PostResponseDTO> dtos = entities.stream()
                .map(post -> PostResponseDTO.convertPostToDTO(post, post.getMember()))
                .collect(Collectors.toList());
        ApiResponse<List<PostResponseDTO>> response = ApiResponse.onSuccess(dtos);
        return ResponseEntity.ok().body(response);

//        List<Post> entities = service.retrievePostsByCategory(category);

    }
    @GetMapping(params = "gender")
    public ResponseEntity<?> findPostsByEnumGender(@RequestParam Gender gender) {

        // 컨버터 생성 (여기서는 Category Enum에 대한 컨버터 생성 예제)
        StringToEnumConverterFactory.StringToEnumConverter<Gender> converter =
                (StringToEnumConverterFactory.StringToEnumConverter<Gender>) factory.getConverter(Gender.class);
        Gender genderEnum = converter.convert(String.valueOf(gender));

        List<Post> entities = service.retrievePostsByGender(genderEnum);
        List<PostResponseDTO> dtos = entities.stream()
                .map(post -> PostResponseDTO.convertPostToDTO(post, post.getMember()))
                .collect(Collectors.toList());
        ApiResponse<List<PostResponseDTO>> response = ApiResponse.onSuccess(dtos);
        return ResponseEntity.ok().body(response);

    }
    @GetMapping(params = "status")
    public ResponseEntity<?> findPostsByEnumStatus(@RequestParam PostStatus status) {

        // 컨버터 생성 (여기서는 Category Enum에 대한 컨버터 생성 예제)
        StringToEnumConverterFactory.StringToEnumConverter<PostStatus> converter =
                (StringToEnumConverterFactory.StringToEnumConverter<PostStatus>) factory.getConverter(PostStatus.class);
        PostStatus statusEnum = converter.convert(String.valueOf(status));

        List<Post> entities = service.retrievePostsByStatus(statusEnum);

        List<PostResponseDTO> dtos = entities.stream()
                .map(post -> PostResponseDTO.convertPostToDTO(post, post.getMember()))
                .collect(Collectors.toList());
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
    @PostMapping() // gender, memberid, category 해결해야함
    public ResponseEntity<?> createPost(@RequestBody @Valid PostRequestDTO requestDTO) {

        //모임 날짜가 현재 시간 이후인지 확인
        LocalDate currentDateTime = LocalDate.now();
        if (requestDTO.getMeetTime().isBefore(currentDateTime)) {
            throw new CustomHandler(ErrorStatus.POST_MEET_TIME_ERROR);
        }

        // 사용자 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(authentication.getName());

        Post newPost = service.createPost(requestDTO, memberId);

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
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePostById(@PathVariable(name = "postId") Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(authentication.getName());

        service.deletePost(postId, memberId);

        ApiResponse<?> response = ApiResponse.successWithoutResult();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(params = {"sortBy", "category", "page"})
    ApiResponse<?> getPostByCategoryAndSort(@RequestParam String category, @RequestParam String sortBy, @RequestParam(defaultValue = "0") Integer page) {
        if (page < 0) {
            throw new CustomHandler(ErrorStatus.INVALID_PAGE_NUMBER);
        }

        Page<Post> posts = service.getPostByCategorySort(category, sortBy, page);

        if (page >= posts.getTotalPages()) {
            throw new CustomHandler(ErrorStatus.INVALID_PAGE_NUMBER);
        }
        return ApiResponse.onSuccess(PostResponseDTO.PagePostListDTO.pagePostListDTO(posts, page));
    }

    @PatchMapping("/{postId}/edit")
    ApiResponse<?> patchPost(@PathVariable Long postId, @RequestBody @Valid PostRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(authentication.getName());
        Post post = service.updatePost(memberId, postId, request);

        postHashtagService.updateHashtag(post, request.getPostHashtagList());
        return ApiResponse.successWithoutResult();
    }
}

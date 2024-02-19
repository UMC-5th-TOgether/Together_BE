package com.backend.together.domain.post.service;

import com.backend.together.domain.block.Entity.Block;
import com.backend.together.domain.block.service.BlockServiceImpl;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import com.backend.together.domain.post.PostImage;
import com.backend.together.domain.post.dto.PostRequestDTO;
import com.backend.together.domain.post.repository.PostImageRepository;
import com.backend.together.global.apiPayload.code.status.ErrorStatus;
import com.backend.together.global.apiPayload.exception.handler.CustomHandler;
import com.backend.together.global.aws.s3.AmazonS3Manager;
import com.backend.together.global.aws.s3.Uuid;
import com.backend.together.global.aws.s3.UuidRepository;
import com.backend.together.global.enums.Category;
import com.backend.together.domain.post.repository.HashtagRepository;
import com.backend.together.global.enums.Gender;
import com.backend.together.global.enums.PostStatus;
import com.backend.together.domain.post.Post;
import com.backend.together.domain.post.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {
    private final MemberRepository memberRepository;
    private final BlockServiceImpl blockService;

    @Autowired
    private PostRepository repository;
    @Autowired
    private PostImageRepository postImageRepository;
    @Autowired
    private HashtagRepository hashtagRepository;
    @Autowired
    private PostHashtagService postHashtagService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UuidRepository uuidRepository;
    @Autowired
    private AmazonS3Manager s3Manager;
    public boolean uploadImages(String postId, MultipartFile[] images) throws Exception {
        Optional<Post> postOptional = repository.findById(Long.parseLong(postId));

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            List<String> imageUrls = new ArrayList<>();

            for (MultipartFile image : images) {
                String uuid = UUID.randomUUID().toString();
                String uuidUrl = uuid + image.getOriginalFilename();
                Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuidUrl).build());

                String imageUrl = s3Manager.uploadFile(s3Manager.generatePostKeyName(savedUuid), image); // 이미지 업로드하기
                imageUrls.add(imageUrl);
                PostImage postImage = PostImage.builder().
                        imageUrl(imageUrl)
                        .post(repository.findById(Long.valueOf(postId)).get()).build();
                postImageRepository.save(postImage);

            }

//            repository.save(post);
            return true;
        } else {
            throw new Exception("작성된 게시글이 없습니다.");
        }
    }


    @Override
    public Post createPost(PostRequestDTO requestDTO, Long memberId) {
        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Post newPost = PostRequestDTO.toEntity(requestDTO, member);
        repository.save(newPost);

        return newPost;
    }
//    public void saveHashtag(Question question, List<String> tagNames) {
//
//        if(tagNames.size() == 0) return;
//
//        tagNames.stream()
//                .map(hashtag ->
//                        hashtagService.findByTagName(hashtag)
//                                .orElseGet(() -> hashtagService.save(hashtag)))
//                .forEach(hashtag -> mapHashtagToQuestion(question, hashtag));
//    }

    @Override
    public void deletePost(Long postId, Long memberId) {
        MemberEntity member = memberRepository.findByMemberId(memberId)
                        .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Post post = repository.findById(postId)
                        .orElseThrow(() -> new CustomHandler(ErrorStatus.POST_NOT_FOUND));

        // 글쓴이가 아니면 삭제 불가 by. Jayde
        if (!post.getMember().equals(member)) {
            throw new CustomHandler(ErrorStatus.INVALID_APPROACH);
        }

        repository.deleteById(postId);
    }

    @Override
    public Post updatePost(Post post) {
        // validate
        return repository.save(post);
    }
    public List<Post> retrieve(){
        System.out.println(repository.findAll());
        return repository.findAll();
    }

    public List<Post> retrievePostByMemberId(Long id){
        MemberEntity member = memberRepository.findByMemberId(id)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return repository.findPostByMember(member);
    }

    @Override
    public Optional<Post> retrievePostById(Long postId) {
        //        if(retrievedPost == null) {} => validate

        return repository.findById(postId); // null은 controller에서 처리
    }

    @Override
    public List<Post> retrievePostsByKeyword(String keyword) {
        List<Post> postByTitleContainingOrContentContaining = repository.findPostByTitleContainingOrContentContaining(keyword, keyword);
        return postByTitleContainingOrContentContaining;
    }

    @Override
    public void updateView(Post post) {
        Long postView = post.getView();

        post.updateView(postView+1);
    }

    @Override
    public Page<Post> getPostByCategorySort(String category, String sort, Integer page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        Category postCategory = switch (category.toUpperCase()) {
            case "HOBBY" -> Category.HOBBY;
            case "PLAY" -> Category.PLAY;
            case "EAT" -> Category.EAT;
            case "EXERCISE" -> Category.EXERCISE;
            default -> throw new CustomHandler(ErrorStatus.CATEGORY_NOT_FOUND);
        };

        List<MemberEntity> blockedList = blockService.getBlockedMember(userId).stream().map(Block::getBlocked).toList();
        Sort sortBy = getSort(sort);
        if (blockedList.isEmpty()) {
            return repository.findAllByCategory(postCategory, PageRequest.of(page, 12, sortBy));
        } else {
            return repository.findAllByCategoryAndMemberNotIn(postCategory, blockedList, PageRequest.of(page, 12, sortBy));
        }
    }

    @Override
    public Page<Post> getPostByMemberId(Long memberId, Integer page) {
        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return repository.findAllByMember(member, PageRequest.of(page, 4, Sort.by("createdAt").descending()));
    }

    @Override
    public Post updatePost(Long memberId, Long postId, PostRequestDTO request) {
        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Post post = repository.findById(postId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.POST_NOT_FOUND));

        if (!post.getMember().equals(member)) {
            throw new CustomHandler(ErrorStatus.INVALID_APPROACH); //작성자가 아니면 수정 불가
        }

        post.setContent(request.getContent());
        post.setPersonNumMin(request.getPersonNumMin());
        post.setPersonNumMax(request.getPersonNumMax());
        post.setMeetTime(request.getMeetTime());
        post.setGender(request.getGender());
        post.setTitle(request.getTitle());

        repository.save(post);

        Post newPost = repository.findById(postId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.POST_NOT_FOUND));

        return newPost;
    }

    private Sort getSort(String sortBy) {
        return switch (sortBy) {
            case "popularity" -> Sort.by("view").descending();
            case "recent" -> Sort.by("createdAt").descending();
            default -> throw new CustomHandler(ErrorStatus.UNSUPORRTED_SORT);
        };
    }

    public List<Post> retrievePostsByCategory(Category category) {
        List<Post> postByCategory = repository.findPostByCategory(category);
        return postByCategory;
    }
    public List<Post> retrievePostsByGender(Gender gender) {
        List<Post> postByGender = repository.findPostByGender(gender);
        return postByGender;
    }
    public List<Post> retrievePostsByStatus(PostStatus status) {
        List<Post> postByStatus = repository.findPostByStatus(status);
        return postByStatus;
    }


    private static void validate(Post post) {
        if(post == null){
            log.warn("Entity cannot be null.");
        }

        assert post != null;
        if(post.getMember().getMemberId() == null){
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }


}

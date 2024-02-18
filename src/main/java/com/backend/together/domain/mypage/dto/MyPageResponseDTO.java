package com.backend.together.domain.mypage.dto;

import com.backend.together.domain.comment.Comment;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.post.Post;
import com.backend.together.domain.review.dto.ReviewResponseDTO;
import com.backend.together.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

public class MyPageResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMyInfoDTO{
        private MyPageResponseDTO.GetMyProfileDTO memberInfo;
        private Long reviewAll;
        private Long reviewEmotionYes;
        private Long avgScore;
        private Double responseRate;

        public static MyPageResponseDTO.GetMyInfoDTO getMyInfoDTO(ReviewResponseDTO.AggregationDTO dto){
            MyPageResponseDTO.GetMyProfileDTO memberInfo = MyPageResponseDTO.GetMyProfileDTO.getMyProfileDTO(dto.getFriend());
            double responseRate = (double) dto.getReviewEmotionYes()/dto.getReviewAll()*100;
            double result = ((double) Math.round(responseRate*100)/100);

            return MyPageResponseDTO.GetMyInfoDTO.builder()
                    .memberInfo(memberInfo)
                    .reviewAll(dto.getReviewAll())
                    .reviewEmotionYes(dto.getReviewEmotionYes())
                    .avgScore(dto.getAvgScore())
                    .responseRate(result)
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMyProfileDTO{
        String profileImg;
        String nickname;
        String gender;
        String age;
        String station;
        String profileMessage;

        public static MyPageResponseDTO.GetMyProfileDTO getMyProfileDTO(MemberEntity member){
            return MyPageResponseDTO.GetMyProfileDTO.builder()
                    .profileImg(member.getImage())
                    .nickname(member.getNickname())
                    .gender(member.getGender())
                    .age(member.getAge())
                    .station(member.getStation())
                    .profileMessage(member.getProfileMessage())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MyPostListDTO {
        Integer pageNo;
        Integer lastPage;
        boolean isLast;
        List<MyPageResponseDTO.MyPostDTO> posts;

        public static MyPageResponseDTO.MyPostListDTO myPostListDTO (Page<Post> post, Integer requestPage) {
            List<MyPageResponseDTO.MyPostDTO> pagePostDTOS = post.stream().map(MyPageResponseDTO.MyPostDTO::myPostDTO).toList();
            return MyPageResponseDTO.MyPostListDTO.builder()
                    .posts(pagePostDTOS)
                    .pageNo(requestPage)
                    .lastPage(post.getTotalPages() - 1)
                    .isLast(requestPage.equals(post.getTotalPages() - 1))
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MyPostDTO {
        Long postId;
        String title;
        String status;

        public static MyPageResponseDTO.MyPostDTO myPostDTO (Post post) {
            return MyPageResponseDTO.MyPostDTO.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .status(post.getStatus().toString())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MyCommentListDTO {
        Integer pageNo;
        Integer lastPage;
        boolean isLast;
        List<MyPageResponseDTO.MyCommentDTO> comments;

        public static MyPageResponseDTO.MyCommentListDTO myCommentListDTO (Page<Comment> comment, Integer requestPage) {
            List<MyPageResponseDTO.MyCommentDTO> pageCommentDTOS = comment.stream().map(MyPageResponseDTO.MyCommentDTO::myCommentDTO).toList();
            return MyPageResponseDTO.MyCommentListDTO.builder()
                    .comments(pageCommentDTOS)
                    .pageNo(requestPage)
                    .lastPage(comment.getTotalPages() - 1)
                    .isLast(requestPage.equals(comment.getTotalPages() - 1))
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MyCommentDTO {
        Long commentId;
        String content;
        Long postId;

        public static MyPageResponseDTO.MyCommentDTO myCommentDTO (Comment comment) {
            return MyPageResponseDTO.MyCommentDTO.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .postId(comment.getPost().getId())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MyReviewListDTO {
        Integer pageNo;
        Integer lastPage;
        boolean isLast;
        List<MyPageResponseDTO.MyReviewDTO> reviews;

        public static MyPageResponseDTO.MyReviewListDTO myReviewListDTO (Page<Review> reviews, Integer requestPage) {
            List<MyPageResponseDTO.MyReviewDTO> pageReviewDTOS = reviews.stream().map(MyPageResponseDTO.MyReviewDTO::myReviewDTO).toList();
            return MyPageResponseDTO.MyReviewListDTO.builder()
                    .reviews(pageReviewDTOS)
                    .pageNo(requestPage)
                    .lastPage(reviews.getTotalPages() - 1)
                    .isLast(requestPage.equals(reviews.getTotalPages() - 1))
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MyReviewDTO {
        Long reviewId;
        String title;

        public static MyPageResponseDTO.MyReviewDTO myReviewDTO (Review review) {
            return MyPageResponseDTO.MyReviewDTO.builder()
                    .reviewId(review.getId())
                    .title(review.getTitle())
                    .build();
        }
    }
}

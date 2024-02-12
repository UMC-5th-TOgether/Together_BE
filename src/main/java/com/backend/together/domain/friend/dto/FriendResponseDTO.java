package com.backend.together.domain.friend.dto;

import com.backend.together.domain.comment.Comment;
import com.backend.together.domain.matching.entity.Matching;
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

public class FriendResponseDTO {
    //내가 매칭 신청 보낸 리스트
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFollowingListDTO{
        List<GetFollowingDTO> followingList;
        Integer listSize;

        public static GetFollowingListDTO getFollowingListDTO(List<Matching> followingList){
            List<GetFollowingDTO> getFollowingDTOList = followingList.stream()
                    .map(GetFollowingDTO::getFollowingDTO).toList();

            return GetFollowingListDTO.builder()
                    .followingList(getFollowingDTOList)
                    .listSize(getFollowingDTOList.size())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFollowingDTO{
        Long matchingId;
        String title;
        String profileImg;
        String nickname;
        String gender;
        String age;

        public static GetFollowingDTO getFollowingDTO(Matching matching){
            return GetFollowingDTO.builder()
                    .matchingId(matching.getId())
                    .title(matching.getTitle())
                    .profileImg(matching.getReceiver().getImage())
                    .nickname(matching.getReceiver().getNickname())
                    .gender(matching.getReceiver().getGender())
                    .age(matching.getReceiver().getAge())
                    .build();
        }
    }

    //내가 받은 매칭 신청 리스트
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFollowerListDTO{
        List<GetFollowerDTO> followerList;
        Integer listSize;

        public static GetFollowerListDTO getFollowerListDTO(List<Matching> followerList){
            List<GetFollowerDTO> getFollowerDTOList = followerList.stream()
                    .map(GetFollowerDTO::getFollowerDTO).toList();

            return GetFollowerListDTO.builder()
                    .followerList(getFollowerDTOList)
                    .listSize(getFollowerDTOList.size())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFollowerDTO{
        Long matchingId;
        String title;
        String profileImg;
        String nickname;
        String gender;
        String age;

        public static GetFollowerDTO getFollowerDTO(Matching matching){
            return GetFollowerDTO.builder()
                    .matchingId(matching.getId())
                    .title(matching.getTitle())
                    .profileImg(matching.getSender().getImage())
                    .nickname(matching.getSender().getNickname())
                    .gender(matching.getReceiver().getGender())
                    .age(matching.getReceiver().getAge())
                    .build();
        }
    }

    //친구 리스트
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFriendListDTO{
        List<GetFriendDTO> friendList;
        Integer listSize;

        public static GetFriendListDTO getFriendListDTO(List<MemberEntity> friendList){
            List<GetFriendDTO> getFriendDTOList = friendList.stream()
                    .map(GetFriendDTO::getFriendDTO).toList();

            return GetFriendListDTO.builder()
                    .friendList(getFriendDTOList)
                    .listSize(getFriendDTOList.size())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFriendDTO{
        Long friendId;
        String profileImg;
        String nickname;
        String gender;
        String age;
        String profileMessage;

        public static GetFriendDTO getFriendDTO(MemberEntity member){
            return GetFriendDTO.builder()
                    .friendId(member.getMemberId())
                    .profileImg(member.getImage())
                    .nickname(member.getNickname())
                    .gender(member.getGender())
                    .age(member.getAge())
                    .profileMessage(member.getProfileMessage())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFriendInfoDTO{
        private GetFriendProfileDTO memberInfo;
        private Long reviewAll;
        private Long reviewEmotionYes;
        private Long avgScore;
        private Double responseRate;

        public static GetFriendInfoDTO getFriendInfoDTO(ReviewResponseDTO.AggregationDTO dto){
            GetFriendProfileDTO memberInfo = GetFriendProfileDTO.getFriendProfileDTO(dto.getFriend());
            double responseRate = (double) dto.getReviewEmotionYes()/dto.getReviewAll()*100;
            double result = ((double) Math.round(responseRate*100)/100);

            return GetFriendInfoDTO.builder()
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
    public static class GetFriendProfileDTO{
        String profileImg;
        String nickname;
        String gender;
        String age;
        String station;
        String profileMessage;

        public static GetFriendProfileDTO getFriendProfileDTO(MemberEntity member){
            return GetFriendProfileDTO.builder()
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
    public static class FriendPostListDTO {
        Integer pageNo;
        Integer lastPage;
        boolean isLast;
        List<FriendResponseDTO.FriendPostDTO> posts;

        public static FriendResponseDTO.FriendPostListDTO friendPostListDTO (Page<Post> post, Integer requestPage) {
            List<FriendResponseDTO.FriendPostDTO> pagePostDTOS = post.stream().map(FriendResponseDTO.FriendPostDTO::friendPostDTO).toList();
            return FriendResponseDTO.FriendPostListDTO.builder()
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
    public static class FriendPostDTO {
        Long postId;
        String title;
        String status;

        public static FriendResponseDTO.FriendPostDTO friendPostDTO (Post post) {
            return FriendResponseDTO.FriendPostDTO.builder()
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
    public static class FriendCommentListDTO {
        Integer pageNo;
        Integer lastPage;
        boolean isLast;
        List<FriendResponseDTO.FriendCommentDTO> comments;

        public static FriendResponseDTO.FriendCommentListDTO friendCommentListDTO (Page<Comment> comment, Integer requestPage) {
            List<FriendResponseDTO.FriendCommentDTO> pageCommentDTOS = comment.stream().map(FriendResponseDTO.FriendCommentDTO::friendCommentDTO).toList();
            return FriendResponseDTO.FriendCommentListDTO.builder()
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
    public static class FriendCommentDTO {
        Long commentId;
        String content;
        Long postId;

        public static FriendResponseDTO.FriendCommentDTO friendCommentDTO (Comment comment) {
            return FriendResponseDTO.FriendCommentDTO.builder()
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
    public static class FriendReviewListDTO {
        Integer pageNo;
        Integer lastPage;
        boolean isLast;
        List<FriendResponseDTO.FriendReviewDTO> reviews;

        public static FriendResponseDTO.FriendReviewListDTO friendReviewListDTO (Page<Review> reviews, Integer requestPage) {
            List<FriendResponseDTO.FriendReviewDTO> pageReviewDTOS = reviews.stream().map(FriendResponseDTO.FriendReviewDTO::friendReviewDTO).toList();
            return FriendResponseDTO.FriendReviewListDTO.builder()
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
    public static class FriendReviewDTO {
        Long reviewId;
        String title;

        public static FriendResponseDTO.FriendReviewDTO friendReviewDTO (Review review) {
            return FriendResponseDTO.FriendReviewDTO.builder()
                    .reviewId(review.getId())
                    .title(review.getTitle())
                    .build();
        }
    }
}

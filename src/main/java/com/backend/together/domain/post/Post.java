package com.backend.together.domain.post;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.global.enums.Category;
import com.backend.together.domain.comment.Comment;
import com.backend.together.global.enums.Gender;
import com.backend.together.global.enums.PostStatus;
//import com.backend.domain.mapping.PostCategory;
import com.backend.together.domain.post.mapping.PostHashtag;
import com.backend.together.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
//@Data
//@Table(name = "Post")
/*
* 24.2.5 모집일자 추가
*
* */
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Column(nullable = false, length = 40)
    private String title;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private Integer personNumMin;

    private Integer personNumMax;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @ColumnDefault(" 'ING' ")
    private PostStatus status;

    // 게시글 조회수 추가 (24.01.13)
    @ColumnDefault("0")
    private Long view;

    // 24.2.5
    @Column(nullable = false)
    private LocalDate meetTime;

    @Enumerated(EnumType.STRING)
    private Category category;


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PostImage> postImageList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PostHashtag> postHashtagList = new ArrayList<>();
    // 24.01.20 추가
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();


    // oneTomany member
    @PrePersist
    public void prePersist() {
        if (this.view == null)
            this.view = 0L;
    }

    public void updateView(Long viewCount) {this.view = viewCount;}

}

package com.backend.together.domain.post;

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
    private Long id;

    @Column(nullable = false, length = 20) // 추후 변경
    private Long memberId;

    @Column(nullable = false, length = 40)
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'NONE'")
    private Gender gender;
    @ColumnDefault("0")
    private Integer personNum;
    @Column(nullable = false, length = 40)
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ING'")
    private PostStatus status;
    // 게시글 조회수 추가 (24.01.13)
    @Column(nullable = false, length = 20)
//    @ColumnDefault("0")
    private Long view;

    // 24.2.5
    private LocalDate meetTime;


    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id")
////    @Column(nullable = false, length = 40)
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'HOBBY'")
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


}

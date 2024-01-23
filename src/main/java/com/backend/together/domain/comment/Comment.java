package com.backend.together.domain.comment;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.post.Post;
import com.backend.together.domain.post.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "comment") // comment는 예약어라 필요
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

//    @ColumnDefault("FALSE")
    @Column(nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // method

    public Comment(String content) {
        this.content = content;
    }

    public void updateWriter(MemberEntity member) {
        this.writer = member;
    }

    public void updateBoard(Post post) {
        this.post =post;
    }

    public void updateParent(Comment comment) {
        this.parent = comment;
    }

    public void changeIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}

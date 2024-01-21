package com.backend.together.domain.comment;

import com.backend.together.domain.post.Post;
import com.backend.together.domain.post.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false, length = 20) // 추후 변경
    private Long member;
    @Column(nullable = false, length = 40)
    private String content;

    @ColumnDefault("1")
    private Integer step;

    @Column(nullable = false, length = 20) // id와 같게 할 예정
    private Integer group;
}

package com.backend.together.domain.post;

import com.backend.together.domain.post.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostImage extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 40)
        private String imageUrl;

        @ManyToOne
        @JoinColumn(name = "post_id")
        private Post post;

}

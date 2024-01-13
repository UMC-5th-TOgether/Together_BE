package com.backend.together.domain.post;

import com.backend.together.domain.mapping.PostHashtag;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = false, length = 40)
    private String hashTag;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL)
    private List<PostHashtag> postList = new ArrayList<>();
}

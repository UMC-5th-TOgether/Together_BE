package com.backend.together.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {

        Long postId;
        LocalDateTime createdAt;

}

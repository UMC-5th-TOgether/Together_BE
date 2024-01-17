package com.backend.together.domain.post.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostResponseDTO<T> {

        private String error;
        private List<T> data;

}

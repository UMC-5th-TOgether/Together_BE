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

/*
* @Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage<T> {
    private final String message;   //메시지
    private final T data;   //어떤 객체의 정보, 리스트 등의 데이터가 이 부분에 담길 수 있다.
    public ResponseMessage(String message, T data){
        this.message = message;
        this.data = data;
    }
}
*
*
*
* */

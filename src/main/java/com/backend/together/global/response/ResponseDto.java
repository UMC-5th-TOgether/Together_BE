package com.backend.together.global.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDto<T> {
    @JsonProperty(value="isSuccess")
    private boolean isSuccess;
    private int code;
    private String message;
    private List<T> data;
}

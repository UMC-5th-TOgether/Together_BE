package com.backend.together.domain.chat.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiRes<T> {
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STAUTS = "error";

    private String status;
    private String message;
    private T data;

    public ApiRes(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiRes<T> successData(T data) {
        return new ApiRes<>(SUCCESS_STATUS, null, data);
    }

    public static <T> ApiRes<T> successMessage(String message) {
        return new ApiRes<>(SUCCESS_STATUS, message, null);
    }

    public static <T> ApiRes<T> error(String message) {
        return new ApiRes<>(ERROR_STAUTS, message, null);
    }
}
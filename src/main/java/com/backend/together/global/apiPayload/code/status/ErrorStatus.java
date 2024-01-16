package com.backend.together.global.apiPayload.code.status;

import com.backend.together.global.apiPayload.code.BaseErrorCode;
import com.backend.together.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),


    // 멤버 관려 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "해당 사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),
    SELF_MATCHING_DECLINE(HttpStatus.BAD_REQUEST, "MEMBER4003", "자기자신과 매칭하는 것은 불가능합니다."),
    // 매칭 관련 에러
    MATCHING_NOT_FOUND(HttpStatus.BAD_REQUEST, "MATCHING4001", "존재하지 않는 매칭 이력입니다."),
    MATCHING_NOT_ACCEPT(HttpStatus.BAD_REQUEST, "MATCHING4002", "수락되지 않은 매칭입니다."),
    // 기타
    INVALID_APPROACH(HttpStatus.BAD_REQUEST, "ETC4001", "잘못된 접근입니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}

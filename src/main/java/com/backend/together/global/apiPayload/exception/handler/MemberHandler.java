package com.backend.together.global.apiPayload.exception.handler;

import com.backend.together.global.apiPayload.code.BaseErrorCode;
import com.backend.together.global.apiPayload.exception.GeneralException;

public class MemberHandler extends GeneralException {
    public MemberHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}

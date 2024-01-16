package com.backend.together.global.apiPayload.exception.handler;

import com.backend.together.global.apiPayload.code.BaseErrorCode;
import com.backend.together.global.apiPayload.exception.GeneralException;

public class MatchingHandler extends GeneralException {
    public MatchingHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}

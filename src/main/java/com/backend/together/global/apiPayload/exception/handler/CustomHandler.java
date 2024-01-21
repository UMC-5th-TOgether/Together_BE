package com.backend.together.global.apiPayload.exception.handler;

import com.backend.together.global.apiPayload.code.BaseErrorCode;
import com.backend.together.global.apiPayload.exception.GeneralException;

public class CustomHandler extends GeneralException {
    public CustomHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}

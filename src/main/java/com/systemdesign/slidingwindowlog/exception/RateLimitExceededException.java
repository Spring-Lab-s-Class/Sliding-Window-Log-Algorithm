package com.systemdesign.slidingwindowlog.exception;

import com.systemdesign.slidingwindowlog.common.exception.BusinessException;
import com.systemdesign.slidingwindowlog.common.exception.ExceptionCode;

public class RateLimitExceededException extends BusinessException {

    public RateLimitExceededException(ExceptionCode exceptionCode, Object... rejectedValues) {
        super(exceptionCode, rejectedValues);
    }
}

package com.systemdesign.slidingwindowlog.common.exception;

public class CommonException extends BusinessException {

    public CommonException(ExceptionCode exceptionCode, Object... rejectedValues) {
        super(exceptionCode, rejectedValues);
    }
}

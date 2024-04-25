package com.systemdesign.slidingwindowlog.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionCode {

    HttpStatus getStatus();

    String getCode();

    String getMessage();
}

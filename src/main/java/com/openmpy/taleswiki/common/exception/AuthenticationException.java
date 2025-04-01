package com.openmpy.taleswiki.common.exception;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {

    private final CustomErrorCode errorCode;
    private final String message;

    public AuthenticationException(final CustomErrorCode errorCode) {
        this(errorCode, errorCode.getMessage());
    }

    public AuthenticationException(final CustomErrorCode errorCode, final String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}

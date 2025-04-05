package com.openmpy.taleswiki.common.exception;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {

    private final CustomErrorCode errorCode;

    public AuthenticationException(final CustomErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

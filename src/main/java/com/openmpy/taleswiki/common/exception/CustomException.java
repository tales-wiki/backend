package com.openmpy.taleswiki.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final CustomErrorCode errorCode;

    public CustomException(final CustomErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(final CustomErrorCode errorCode, final String message) {
        super(errorCode.getMessage() + " " + message);
        this.errorCode = errorCode;
    }
}

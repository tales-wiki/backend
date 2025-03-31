package com.openmpy.taleswiki.common.exception;

public record ErrorResponse(
        String code,
        String message
) {

    public static ErrorResponse of(final CustomErrorCode errorCode) {
        return new ErrorResponse(errorCode.name(), errorCode.getMessage());
    }

    public static ErrorResponse of(final CustomErrorCode errorCode, final String errorMessage) {
        return new ErrorResponse(errorCode.name(), errorMessage);
    }
}

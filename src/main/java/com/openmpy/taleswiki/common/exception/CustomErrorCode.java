package com.openmpy.taleswiki.common.exception;

import lombok.Getter;

@Getter
public enum CustomErrorCode {

    // article domain
    NOT_ALLOWED_VERSION_NUMBER_ZERO_OR_NEGATIVE("버전 값이 0 또는 음수일 수 없습니다. [%d]"),

    // server
    REQUEST_METHOD_NOT_SUPPORTED("지원하지 않는 요청 메서드입니다."),
    NO_RESOURCE_REQUEST("존재하지 않는 리소스입니다."),
    MESSAGE_NOT_READABLE("읽을 수 없는 요청입니다."),
    INTERNAL_SERVER_ERROR("서버 내부에서 에러가 발생했습니다."),
    ;

    private final String message;

    CustomErrorCode(final String message) {
        this.message = message;
    }
}

package com.openmpy.taleswiki.common.exception;

import lombok.Getter;

@Getter
public enum CustomErrorCode {

    // article domain
    NOT_FOUND_ARTICLE_CATEGORY("찾을 수 없는 카테고리입니다. [%s]"),
    NOT_ALLOWED_ARTICLE_IP_NULL_AND_BLANK("Ip가 빈 값일 수 없습니다."),
    INVALID_ARTICLE_IP("올바르지 않은 Ip 입니다."),
    NOT_ALLOWED_ARTICLE_NICKNAME_NULL_AND_BLANK("닉네임이 빈 값일 수 없습니다."),
    NOT_ALLOWED_ARTICLE_SIZE_NEGATIVE("크기 값이 음수일 수 없습니다. [%d]"),
    NOT_ALLOWED_ARTICLE_TITLE_NULL_AND_BLANK("제목이 빈 값일 수 없습니다."),
    NOT_ALLOWED_ARTICLE_VERSION_NUMBER_ZERO_OR_NEGATIVE("버전 값이 0 또는 음수일 수 없습니다. [%d]"),

    // article service
    ALREADY_WRITTEN_ARTICLE_TITLE_AND_CATEGORY("해당 카테고리에 이미 작성된 글입니다. [카테고리: %s, 제목: %s]"),
    NOT_FOUND_ARTICLE_VERSION("찾을 수 없는 버전의 게시글 번호입니다. [ID: %d, 버전: %d]"),
    NOT_FOUND_ARTICLE_ID("찾을 수 없는 게시글 번호입니다. [ID: %d]"),

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

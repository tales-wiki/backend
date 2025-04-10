package com.openmpy.taleswiki.common.exception;

import lombok.Getter;

@Getter
public enum CustomErrorCode {

    // member
    NOT_ALLOWED_MEMBER_EMAIL_NULL_AND_BLANK("회원 이메일이 빈 값일 수 없습니다."),
    INVALID_MEMBER_EMAIL("올바르지 않은 회원 이메일입니다."),
    FAILED_KAKAO_LOGIN("카카오 로그인에 실패했습니다."),
    FAILED_GOOGLE_LOGIN("구글 로그인에 실패했습니다."),
    NOT_FOUND_MEMBER_ID("찾을 수 없는 회원 번호입니다."),

    // article
    NOT_FOUND_ARTICLE_CATEGORY("찾을 수 없는 카테고리입니다."),
    NOT_ALLOWED_ARTICLE_NICKNAME_NULL_AND_BLANK("게시글 작성자명이 빈 값일 수 없습니다."),
    NOT_ALLOWED_ARTICLE_SIZE_NEGATIVE("게시글 문서 크기가 음수일 수 없습니다."),
    NOT_ALLOWED_ARTICLE_TITLE_NULL_AND_BLANK("게시글 제목이 빈 값일 수 없습니다."),
    NOT_ALLOWED_ARTICLE_VERSION_NUMBER_ZERO_OR_NEGATIVE("게시글 버전 값이 0 이하일 수 없습니다."),
    INVALID_ARTICLE_TITLE_LENGTH("게시글 제목 길이가 올바르지 않습니다. (최대 12자)"),
    INVALID_ARTICLE_TITLE("올바르지 않은 게시글 제목입니다."),
    INVALID_ARTICLE_NICKNAME_LENGTH("게시글 작성자명 길이가 올바르지 않습니다. (최대 10자)"),
    NOT_ALLOWED_ARTICLE_REPORT_REASON_NULL_AND_BLANK("게시글 신고 내용이 빈 값일 수 없습니다"),
    INVALID_ARTICLE_REPORT_REASON_LENGTH("게시글 신고 내용 길이가 올바르지 않습니다. (최소 10자, 최대 100자)"),
    ALREADY_WRITTEN_ARTICLE_TITLE_AND_CATEGORY("해당 카테고리에 이미 작성된 게시글입니다."),
    NOT_FOUND_ARTICLE_VERSION_ID("찾을 수 없는 게시글 버전 번호입니다."),
    NOT_FOUND_ARTICLE_ID("찾을 수 없는 게시글 번호입니다."),
    NO_EDITING_ARTICLE("편집 할 수 없는 게시글입니다."),
    ALREADY_ARTICLE_REPORT_VERSION_ID("이미 신고한 게시글 버전 번호입니다."),

    // admin
    INVALID_MEMBER_AUTHORITY("올바르지 않은 회원 권한입니다."),
    ALREADY_BLOCKED_IP("이미 정지 된 IP 주소입니다."),
    NOT_FOUND_BLOCKED_IP("찾을 수 없는 IP 주소입니다."),

    // other
    NOT_FOUND_COOKIE("찾을 수 없는 쿠키입니다."),
    INVALID_ACCESS_TOKEN("올바르지 않은 액세스 토큰입니다."),
    NOT_ALLOWED_IP_NULL_AND_BLANK("IP가 빈 값일 수 없습니다."),
    INVALID_IP("올바르지 않은 IP 주소입니다."),
    BLOCKED_IP("정지된 IP 주소입니다."),
    DISCORD_ERROR("디스코드에서 에러가 발생했습니다."),

    // server
    REQUEST_METHOD_NOT_SUPPORTED("지원하지 않는 요청 메서드입니다."),
    METHOD_ARGUMENT_NOT_VALID("유효성 에러가 발생했습니다."),
    NO_RESOURCE_REQUEST("존재하지 않는 리소스입니다."),
    MESSAGE_NOT_READABLE("읽을 수 없는 요청입니다."),
    INTERNAL_SERVER_ERROR("서버 내부에서 에러가 발생했습니다."),
    FORBIDDEN("접근할 수 없습니다."),
    ;

    private final String message;

    CustomErrorCode(final String message) {
        this.message = message;
    }
}

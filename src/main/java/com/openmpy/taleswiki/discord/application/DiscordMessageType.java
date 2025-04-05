package com.openmpy.taleswiki.discord.application;

import lombok.Getter;

@Getter
public enum DiscordMessageType {

    SIGNUP_MESSAGE(
            """
                    ```
                    ID: %d
                    이메일: %s
                    소셜: %s
                    날짜: %s```"""
    ),
    ARTICLE_REPORT_MESSAGE(
            """
                    ```
                    [숨김]
                                        
                    게시글 ID: %d
                    제목: %s
                    카테고리: %s
                    신고 내용: %s
                    날짜: %s```"""
    ),
    WARNING_MESSAGE(
            """
                    ```
                    Error Code: %s
                    Error Message: %s
                    Request Uri: %s %s
                    Request Payload: %s
                    IP: %s
                    날짜: %s```"""
    ),
    ERROR_MESSAGE(
            """
                    ```
                    Error Message: %s
                    Request Uri: %s %s
                    Request Payload: %s
                    IP: %s
                    날짜: %s```"""
    );

    private final String value;

    DiscordMessageType(final String value) {
        this.value = value;
    }
}

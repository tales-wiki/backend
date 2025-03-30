package com.openmpy.taleswiki.article.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ArticleNickname {

    private String value;

    public ArticleNickname(final String value) {
        validateBlank(value);

        this.value = value;
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("닉네임이 빈 값일 수 없습니다.");
        }
    }
}

package com.openmpy.taleswiki.article.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ArticleSize {

    private static final int INVALID_CONTENT_SIZE = 0;

    private int value;

    public ArticleSize(final int value) {
        validateSize(value);

        this.value = value;
    }

    private void validateSize(final int value) {
        if (value < INVALID_CONTENT_SIZE) {
            final String error = String.format("크기 값이 음수일 수 없습니다. [%d]", value);
            throw new IllegalArgumentException(error);
        }
    }
}

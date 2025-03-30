package com.openmpy.taleswiki.article.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ArticleVersionNumber {

    private static final int INVALID_VERSION_NUMBER = 0;

    private int value;

    public ArticleVersionNumber(final int value) {
        validateVersionNumber(value);

        this.value = value;
    }

    private void validateVersionNumber(final int value) {
        if (value <= INVALID_VERSION_NUMBER) {
            final String error = String.format("버전 값이 0 또는 음수일 수 없습니다. [%d]", value);
            throw new IllegalArgumentException(error);
        }
    }
}

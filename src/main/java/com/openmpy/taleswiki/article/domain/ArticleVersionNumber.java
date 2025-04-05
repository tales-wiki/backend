package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_ARTICLE_VERSION_NUMBER_ZERO_OR_NEGATIVE;

import com.openmpy.taleswiki.common.exception.CustomException;
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
            throw new CustomException(NOT_ALLOWED_ARTICLE_VERSION_NUMBER_ZERO_OR_NEGATIVE);
        }
    }
}

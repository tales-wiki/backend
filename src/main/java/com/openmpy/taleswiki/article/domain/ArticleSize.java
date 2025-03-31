package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_ARTICLE_SIZE_NEGATIVE;

import com.openmpy.taleswiki.common.exception.CustomException;
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
            throw new CustomException(NOT_ALLOWED_ARTICLE_SIZE_NEGATIVE, value);
        }
    }
}

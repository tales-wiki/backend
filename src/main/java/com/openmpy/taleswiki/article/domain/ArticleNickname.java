package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_ARTICLE_NICKNAME_LENGTH;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_ARTICLE_NICKNAME_NULL_AND_BLANK;

import com.openmpy.taleswiki.common.exception.CustomException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ArticleNickname {

    private static final int MAX_NICKNAME_LENGTH = 10;

    private String value;

    public ArticleNickname(final String value) {
        validateBlank(value);
        validateLength(value);

        this.value = value.trim();
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException(NOT_ALLOWED_ARTICLE_NICKNAME_NULL_AND_BLANK);
        }
    }

    private void validateLength(final String value) {
        if (value.length() > MAX_NICKNAME_LENGTH) {
            throw new CustomException(INVALID_ARTICLE_NICKNAME_LENGTH);
        }
    }
}

package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_ARTICLE_TITLE;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_ARTICLE_TITLE_LENGTH;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_ARTICLE_TITLE_NULL_AND_BLANK;

import com.openmpy.taleswiki.common.exception.CustomException;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ArticleTitle {

    private static final int MAX_TITLE_LENGTH = 12;
    private static final String INVALID_TITLE_PATTERN = "^[a-zA-Z0-9가-힣]+$";

    private String value;

    public ArticleTitle(final String value) {
        validateBlank(value);
        validateLength(value);
        validateTitle(value);

        this.value = value;
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException(NOT_ALLOWED_ARTICLE_TITLE_NULL_AND_BLANK);
        }
    }

    private void validateLength(final String value) {
        if (value.length() > MAX_TITLE_LENGTH) {
            throw new CustomException(INVALID_ARTICLE_TITLE_LENGTH);
        }
    }

    private void validateTitle(final String value) {
        if (!isValidTitle(value)) {
            throw new CustomException(INVALID_ARTICLE_TITLE);
        }
    }

    private boolean isValidTitle(final String value) {
        return Pattern.matches(INVALID_TITLE_PATTERN, value);
    }
}

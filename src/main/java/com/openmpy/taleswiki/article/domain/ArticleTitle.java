package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_ARTICLE_TITLE_NULL_AND_BLANK;

import com.openmpy.taleswiki.common.exception.CustomException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ArticleTitle {

    private String value;

    public ArticleTitle(final String value) {
        validateBlank(value);

        this.value = value;
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException(NOT_ALLOWED_ARTICLE_TITLE_NULL_AND_BLANK);
        }
    }
}

package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_ARTICLE_REPORT_REASON_LENGTH;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_ARTICLE_REPORT_REASON_NULL_AND_BLANK;

import com.openmpy.taleswiki.common.exception.CustomException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ArticleReportReason {

    private static final int ARTICLE_REPORT_REASON_MIN_LENGTH = 10;
    private static final int ARTICLE_REPORT_REASON_MAX_LENGTH = 100;

    private String value;

    public ArticleReportReason(final String value) {
        validateBlank(value);
        validateLength(value);

        this.value = value;
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException(NOT_ALLOWED_ARTICLE_REPORT_REASON_NULL_AND_BLANK);
        }
    }

    private void validateLength(final String value) {
        if (value.length() < ARTICLE_REPORT_REASON_MIN_LENGTH || value.length() > ARTICLE_REPORT_REASON_MAX_LENGTH) {
            throw new CustomException(INVALID_ARTICLE_REPORT_REASON_LENGTH);
        }
    }
}

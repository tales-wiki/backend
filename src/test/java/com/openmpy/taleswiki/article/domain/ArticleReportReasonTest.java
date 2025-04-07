package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_ARTICLE_REPORT_REASON_LENGTH;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_ARTICLE_REPORT_REASON_NULL_AND_BLANK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ArticleReportReasonTest {

    @DisplayName("[통과] 게시글 신고 내용 객체를 생성한다.")
    @Test
    void article_report_reason_test_01() {
        // given
        final String value = "내용".repeat(5);

        // when
        final ArticleReportReason reportReason = new ArticleReportReason(value);

        // then
        assertThat(reportReason.getValue()).isEqualTo("내용내용내용내용내용");
    }

    @DisplayName("[예외] 게시글 신고 내용이 null 또는 빈 값일 수 없다.")
    @ParameterizedTest(name = "값: {0}")
    @NullAndEmptySource
    void 예외_article_report_reason_test_01(final String value) {
        // when & then
        assertThatThrownBy(() -> new ArticleReportReason(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_ALLOWED_ARTICLE_REPORT_REASON_NULL_AND_BLANK.getMessage());
    }

    @DisplayName("[예외] 게시글 신고 내용 길이가 10자 미만이다.")
    @Test
    void 예외_article_report_reason_test_02() {
        // given
        final String value = "1".repeat(9);

        // when & then
        assertThatThrownBy(() -> new ArticleReportReason(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_ARTICLE_REPORT_REASON_LENGTH.getMessage());
    }

    @DisplayName("[예외] 게시글 신고 내용 길이가 100자를 초과한다.")
    @Test
    void 예외_article_report_reason_test_03() {
        // given
        final String value = "1".repeat(101);

        // when & then
        assertThatThrownBy(() -> new ArticleReportReason(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_ARTICLE_REPORT_REASON_LENGTH.getMessage());
    }
}
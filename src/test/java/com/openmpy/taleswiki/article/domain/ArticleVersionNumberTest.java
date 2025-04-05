package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_ARTICLE_VERSION_NUMBER_ZERO_OR_NEGATIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ArticleVersionNumberTest {

    @DisplayName("[통과] 게시글 버전 값 객체가 정상적으로 생성된다.")
    @Test
    void article_version_number_test_01() {
        // given
        final int value = 123;

        // when
        final ArticleVersionNumber versionNumber = new ArticleVersionNumber(value);

        // then
        assertThat(versionNumber.getValue()).isEqualTo(123);
    }

    @DisplayName("[예외] 게시글 버전 값이 0 또는 음수일 수 없다.")
    @ParameterizedTest(name = "값: {0}")
    @ValueSource(ints = {-1, 0})
    void 예외_article_version_number_test_01(final int value) {
        // when & then
        assertThatThrownBy(() -> new ArticleVersionNumber(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_ALLOWED_ARTICLE_VERSION_NUMBER_ZERO_OR_NEGATIVE.getMessage());
    }
}
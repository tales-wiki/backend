package com.openmpy.taleswiki.article.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @DisplayName("[통과] 게시글 버전 값 객체의 기본 값은 1이다.")
    @Test
    void article_version_number_test_02() {

        // when
        final ArticleVersionNumber versionNumber = new ArticleVersionNumber();

        // then
        assertThat(versionNumber.getValue()).isEqualTo(1);
    }

    @DisplayName("[예외] 게시글 버전 값이 0 또는 음수일 수 없다.")
    @ParameterizedTest(name = "값: {0}")
    @ValueSource(ints = {-1, 0})
    void 예외_article_version_number_test_01(final int value) {
        // given
        final String error = String.format("버전 값이 0 또는 음수일 수 없습니다. [%d]", value);

        // when & then
        assertThatThrownBy(() -> new ArticleVersionNumber(value)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(error);
    }
}
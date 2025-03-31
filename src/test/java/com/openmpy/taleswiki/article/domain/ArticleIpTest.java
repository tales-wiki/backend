package com.openmpy.taleswiki.article.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ArticleIpTest {

    @DisplayName("[통과] 게시글 아이피 객체가 정상적으로 생성된다.")
    @ParameterizedTest(name = "값: {0}")
    @ValueSource(strings = {"127.0.0.1", "0000:0000:0000:0000:0000:0000:0000:0000"})
    void article_ip_test_01(final String value) {
        // when
        final ArticleIp ip = new ArticleIp(value);

        // then
        assertThat(ip.getValue()).isEqualTo(value);
    }

    @DisplayName("[예외] 아이피가 빈 값이다.")
    @ParameterizedTest(name = "값: {0}")
    @NullAndEmptySource
    void 예외_article_ip_test_01(final String value) {
        // when & then
        assertThatThrownBy(() -> new ArticleIp(value)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Ip가 빈 값일 수 없습니다.");
    }

    @DisplayName("[예외] 아이피 값이 올바르지 않다.")
    @ParameterizedTest(name = "값: {0}")
    @ValueSource(strings = {"0", "0.0", "0.0.0", "1:1", "1:1:1"})
    void 예외_article_ip_test_02(final String value) {
        // when & then
        assertThatThrownBy(() -> new ArticleIp(value)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않은 Ip 입니다.");
    }
}
package com.openmpy.taleswiki.article.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ArticleNicknameTest {

    @DisplayName("[통과] 게시글 닉네임 객체가 정상적으로 생성된다.")
    @Test
    void article_nickname_test_01() {
        // given
        final String value = "홍길동";

        // when
        final ArticleNickname nickname = new ArticleNickname(value);

        // then
        assertThat(nickname.getValue()).isEqualTo("홍길동");
    }

    @DisplayName("[예외] 게시글 닉네임 객체가 빈 값일 수 없다.")
    @ParameterizedTest(name = "값: {0}")
    @NullAndEmptySource
    void 예외_article_nickname_test_01(final String value) {
        // when & then
        assertThatThrownBy(() -> new ArticleNickname(value)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("닉네임이 빈 값일 수 없습니다.");
    }
}
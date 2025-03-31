package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_ARTICLE_NICKNAME_NULL_AND_BLANK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ArticleNicknameTest {

    @DisplayName("[통과] 게시글 닉네임 객체가 정상적으로 생성된다.")
    @Test
    void article_nickname_test_01() {
        // given
        final String value = "초원";

        // when
        final ArticleNickname nickname = new ArticleNickname(value);

        // then
        assertThat(nickname.getValue()).isEqualTo("초원");
    }

    @DisplayName("[예외] 게시글 닉네임 객체가 빈 값일 수 없다.")
    @ParameterizedTest(name = "값: {0}")
    @NullAndEmptySource
    void 예외_article_nickname_test_01(final String value) {
        // when & then
        assertThatThrownBy(() -> new ArticleNickname(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_ALLOWED_ARTICLE_NICKNAME_NULL_AND_BLANK.getMessage());
    }
}
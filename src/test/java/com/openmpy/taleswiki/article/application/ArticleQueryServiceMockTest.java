package com.openmpy.taleswiki.article.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.presentation.response.ArticleRandomResponse;
import com.openmpy.taleswiki.common.component.RandomGenerator;
import com.openmpy.taleswiki.common.exception.CustomException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleQueryServiceMockTest {

    @InjectMocks
    private ArticleQueryService articleQueryService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private RandomGenerator randomGenerator;

    @DisplayName("[통과] 최신 버전의 게시글 ID를 랜덤으로 조회한다.")
    @Test
    void article_query_service_test_01() {
        // given
        final Long articleMaxId = 10L;
        final Long articleVersionId = 100L;
        final Long randomId = 5L;

        final ArticleVersion mockVersion = mock(ArticleVersion.class);
        final Article mockArticle = mock(Article.class);

        // stub
        when(mockArticle.getLatestVersion()).thenReturn(mockVersion);
        when(mockVersion.getId()).thenReturn(articleVersionId);

        when(articleRepository.findByMaxId()).thenReturn(articleMaxId);
        when(randomGenerator.generate(articleMaxId)).thenReturn(randomId);
        when(articleRepository.findById(randomId)).thenReturn(Optional.of(mockArticle));

        // when
        final ArticleRandomResponse response = articleQueryService.randomArticle();

        // then
        assertThat(response.articleVersionId()).isEqualTo(articleVersionId);
    }

    @DisplayName("[예외] 게시글 랜덤 조회를 모두 실패한다.")
    @Test
    void 예외_article_query_service_test_02() {
        // given
        final Long articleMaxId = 10L;

        // stub
        when(articleRepository.findByMaxId()).thenReturn(articleMaxId);
        when(randomGenerator.generate(articleMaxId)).thenReturn(
                1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L
        );
        when(articleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> articleQueryService.randomArticle())
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_FOUND_ARTICLE_ID.getMessage());
    }
}
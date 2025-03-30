package com.openmpy.taleswiki.article.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.response.ArticleCreateResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadResponse;
import com.openmpy.taleswiki.dummy.Fixture;
import com.openmpy.taleswiki.support.annotation.CustomServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@CustomServiceTest
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @DisplayName("[통과] 게시글을 작성한다.")
    @Test
    void article_service_test_01() {
        // given
        final ArticleCreateRequest request = new ArticleCreateRequest("제목", "닉네임", "인물", "내용");

        // when
        final ArticleCreateResponse response = articleService.create(request);

        // then
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.nickname()).isEqualTo("닉네임");
        assertThat(response.category()).isEqualTo("인물");
        assertThat(response.version()).isEqualTo(1);
    }

    @DisplayName("[통과] 단건 버전의 게시글을 조회한다.")
    @Test
    void article_service_test_02() {
        // given
        final Article article = Fixture.createArticle();
        final ArticleVersion version = new ArticleVersion("버전1", 1, article);

        article.addVersion(version);
        final Article savedArticle = articleRepository.save(article);

        // when
        final ArticleReadResponse response = articleService.read(savedArticle.getId());

        // then
        assertThat(response.title()).isEqualTo("제목입니다.");
        assertThat(response.nickname()).isEqualTo("닉네임입니다.");
        assertThat(response.content()).isEqualTo("버전1");
        assertThat(response.latestVersion()).isEqualTo(1);
        assertThat(response.latestUpdatedAt()).isNotNull();
    }

    @DisplayName("[통과] 게시글의 버전이 여러개일 경우 최신 버전을 조회한다.")
    @Test
    void article_service_test_03() {
        // given
        final Article article = Fixture.createArticle();
        final ArticleVersion version01 = new ArticleVersion("버전1", 1, article);
        final ArticleVersion version02 = new ArticleVersion("버전2", 2, article);

        article.addVersion(version01);
        article.addVersion(version02);
        final Article savedArticle = articleRepository.save(article);

        // when
        final ArticleReadResponse response = articleService.read(savedArticle.getId());

        // then
        assertThat(response.title()).isEqualTo("제목입니다.");
        assertThat(response.nickname()).isEqualTo("닉네임입니다.");
        assertThat(response.content()).isEqualTo("버전2");
        assertThat(response.latestVersion()).isEqualTo(2);
        assertThat(response.latestUpdatedAt()).isNotNull();
    }

    @DisplayName("[예외] 해당 카테고리에 이미 작성된 게시글이 존재한다.")
    @Test
    void 예외_article_service_test_01() {
        // given
        final Article article = Fixture.ARTICLE01;
        final ArticleCreateRequest request = new ArticleCreateRequest("제목입니다.", "닉네임", "인물", "내용");

        articleRepository.save(article);

        // when & then
        assertThatThrownBy(() -> articleService.create(request)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 카테고리에 이미 작성된 글입니다. [카테고리: 인물, 제목: 제목입니다.]");
    }
}
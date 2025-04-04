package com.openmpy.taleswiki.article.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.ALREADY_WRITTEN_ARTICLE_TITLE_AND_CATEGORY;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_ID;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_VERSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.request.ArticleUpdateRequest;
import com.openmpy.taleswiki.article.presentation.response.ArticleCreateResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllByCategoryResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllRecentEditsResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllVersionsResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadByCategoryResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadByVersionResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadRecentEditsResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadVersionResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleSearchAllResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleSearchResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleUpdateResponse;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.dummy.Fixture;
import com.openmpy.taleswiki.member.application.MemberService;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.support.annotation.CustomServiceTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@CustomServiceTest
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @MockitoBean
    private MemberService memberService;

    @DisplayName("[통과] 게시글을 작성한다.")
    @Test
    void article_service_test_01() {
        // given
        final ArticleCreateRequest request = new ArticleCreateRequest("제목", "닉네임", "인물", "내용");

        // when
        final ArticleCreateResponse response = articleService.create(request, Fixture.createMockServetRequest(10));

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
        final Article article = Fixture.createArticleWithVersion();
        final Article savedArticle = articleRepository.save(article);

        // when
        final ArticleReadResponse response = articleService.read(savedArticle.getId());

        // then
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.nickname()).isEqualTo("초원");
        assertThat(response.content()).isEqualTo("버전1");
        assertThat(response.version()).isEqualTo(1);
        assertThat(response.createdAt()).isNotNull();
    }

    @DisplayName("[통과] 게시글의 버전이 여러개일 경우 최신 버전을 조회한다.")
    @Test
    void article_service_test_03() {
        // given
        final Article article = Fixture.createArticleWithVersions();
        final Article savedArticle = articleRepository.save(article);

        // when
        final ArticleReadResponse response = articleService.read(savedArticle.getId());

        // then
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.nickname()).isEqualTo("밍밍");
        assertThat(response.content()).isEqualTo("버전2");
        assertThat(response.version()).isEqualTo(2);
        assertThat(response.createdAt()).isNotNull();
    }

    @DisplayName("[통과] 게시글의 버전 목록을 조회한다.")
    @Test
    void article_service_test_04() {
        // given
        final Article article = Fixture.createArticleWithVersions();
        final Article savedArticle = articleRepository.save(article);

        // when
        final ArticleReadAllVersionsResponse response = articleService.readAllVersions(savedArticle.getId());
        final List<ArticleReadVersionResponse> responses = response.responses();

        // then
        assertThat(response.title()).isEqualTo("제목");
        assertThat(responses.getFirst().nickname()).isEqualTo("밍밍");
        assertThat(responses.getFirst().version()).isEqualTo(2);
        assertThat(responses.getLast().nickname()).isEqualTo("초원");
        assertThat(responses.getLast().version()).isEqualTo(1);
        assertThat(responses).hasSize(2);
    }

    @DisplayName("[통과] 게시글의 특정 버전을 조회한다.")
    @Test
    void article_service_test_05() {
        // given
        final Article article = Fixture.createArticleWithVersions();
        final Article savedArticle = articleRepository.save(article);

        // when
        final ArticleReadByVersionResponse response = articleService.readByVersion(savedArticle.getId(), 1);

        // then
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.nickname()).isEqualTo("초원");
        assertThat(response.content()).isEqualTo("버전1");
        assertThat(response.createdAt()).isNotNull();
    }

    @DisplayName("[통과] 게시글을 수정한다.")
    @Test
    void article_service_test_06() {
        // given
        final Member member = Fixture.createMember();
        final Article article = Fixture.createArticleWithVersion();
        final Article savedArticle = articleRepository.save(article);

        final ArticleUpdateRequest request = new ArticleUpdateRequest("수정제목", "수정된 닉네임", "수정된 내용");

        // stub
        when(memberService.getMember(anyLong())).thenReturn(member);

        // when
        final ArticleUpdateResponse response = articleService.update(
                member.getId(),
                savedArticle.getId(),
                request,
                Fixture.createMockServetRequest(10)
        );

        // then
        assertThat(response.id()).isEqualTo(savedArticle.getId());
        assertThat(response.title()).isEqualTo("수정제목");
        assertThat(response.nickname()).isEqualTo("수정된 닉네임");
        assertThat(response.content()).isEqualTo("수정된 내용");
        assertThat(response.version()).isEqualTo(2);
    }

    @DisplayName("[통과] 게시글을 삭제한다.")
    @Test
    void article_service_test_07() {
        // given
        final Article article = Fixture.createArticleWithVersion();
        final Article savedArticle = articleRepository.save(article);

        // when
        articleService.delete(savedArticle.getId());

        // then
        final Optional<Article> foundArticle = articleRepository.findById(savedArticle.getId());
        assertThat(foundArticle).isEmpty();
    }

    @DisplayName("[통과] 카테고리에 해당하는 게시글 목록을 조회한다.")
    @Test
    void article_service_test_08() {
        // given
        final List<Article> articles = Fixture.createArticles();
        articleRepository.saveAll(articles);

        // when
        final ArticleReadAllByCategoryResponse responseByPerson =
                articleService.readAllByCategory(ArticleCategory.PERSON);
        final ArticleReadAllByCategoryResponse responseByGuild =
                articleService.readAllByCategory(ArticleCategory.GUILD);

        final List<ArticleReadByCategoryResponse> responsesByPerson = responseByPerson.responses();
        final List<ArticleReadByCategoryResponse> responsesByGuild = responseByGuild.responses();

        // then
        assertThat(responseByPerson.size()).isEqualTo(2);
        assertThat(responsesByPerson.getFirst().title()).isEqualTo("제목01");
        assertThat(responsesByPerson.getLast().title()).isEqualTo("제목02");

        assertThat(responseByGuild.size()).isEqualTo(2);
        assertThat(responsesByGuild.getFirst().title()).isEqualTo("제목01");
        assertThat(responsesByGuild.getLast().title()).isEqualTo("제목02");
    }

    @DisplayName("[통과] 최근 편집된 게시글 10개를 편집된 날짜를 기준으로 내림차순 조회한다.")
    @Test
    void article_service_test_09() {
        // given
        final List<Article> articles = Fixture.createArticlesWithVersions();
        articleRepository.saveAll(articles);

        // when
        final ArticleReadAllRecentEditsResponse response = articleService.readAllRecentEdits();

        // then
        final List<ArticleReadRecentEditsResponse> responses = response.responses();

        assertThat(responses).hasSize(10);
        assertThat(responses.getFirst().title()).isEqualTo("제목11");
        assertThat(responses.getLast().title()).isEqualTo("제목02");
        assertThat(responses.getFirst().createdAt().isAfter(responses.getLast().createdAt())).isTrue();
    }

    @DisplayName("[통과] 게시글에서 제목을 검색한다.")
    @Test
    void article_service_test_10() {
        // given
        final List<Article> articles = Fixture.createArticles();
        articleRepository.saveAll(articles);

        // when
        final ArticleSearchAllResponse response = articleService.search("제목");

        // then
        final List<ArticleSearchResponse> responses = response.responses();

        assertThat(responses).hasSize(4);
        assertThat(responses.getFirst().title()).isEqualTo("제목02");
        assertThat(responses.getFirst().category()).isEqualTo("GUILD");
        assertThat(responses.getLast().title()).isEqualTo("제목01");
        assertThat(responses.getLast().category()).isEqualTo("PERSON");
    }

    @DisplayName("[통과] 게시글을 검색하지만 아무것도 찾지 못한다.")
    @Test
    void article_service_test_11() {
        // given
        final List<Article> articles = Fixture.createArticles();
        articleRepository.saveAll(articles);

        // when
        final ArticleSearchAllResponse response = articleService.search("목제");

        // then
        final List<ArticleSearchResponse> responses = response.responses();

        assertThat(responses).isEmpty();
    }

    @DisplayName("[예외] 해당 카테고리에 이미 작성된 게시글이 존재한다.")
    @Test
    void 예외_article_service_test_01() {
        // given
        final Article article = Fixture.ARTICLE01;
        final ArticleCreateRequest request = new ArticleCreateRequest("제목", "초원", "인물", "내용");

        articleRepository.save(article);

        // when & then
        final String error = String.format(ALREADY_WRITTEN_ARTICLE_TITLE_AND_CATEGORY.getMessage(), "인물", "제목");
        final MockHttpServletRequest mockServetRequest = Fixture.createMockServetRequest(10);

        assertThatThrownBy(() -> articleService.create(request, mockServetRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(error);
    }

    @DisplayName("[예외] 게시글 번호를 찾을 수 없다.")
    @Test
    void 예외_article_service_test_02() {
        // when & then
        final String error = String.format(NOT_FOUND_ARTICLE_ID.getMessage(), 1L);

        assertThatThrownBy(() -> articleService.getArticle(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(error);
    }

    @DisplayName("[예외] 게시글 버전을 조회할 수 없다.")
    @Test
    void 예외_article_service_test_03() {
        // given
        final Article article = Fixture.createArticle();
        final Article savedArticle = articleRepository.save(article);
        final Long articleId = savedArticle.getId();
        final int version = 1;

        // when & then
        final String error = String.format(NOT_FOUND_ARTICLE_VERSION.getMessage(), articleId, version);

        assertThatThrownBy(() -> articleService.readByVersion(articleId, version))
                .isInstanceOf(CustomException.class)
                .hasMessage(error);
    }
}
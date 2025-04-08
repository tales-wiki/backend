package com.openmpy.taleswiki.article.presentation;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openmpy.taleswiki.article.presentation.response.ArticleReadCategoryResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadCategoryResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadLatestUpdateResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadLatestUpdateResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleSearchResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleSearchResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleVersionReadArticleResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleVersionReadArticleResponses;
import com.openmpy.taleswiki.support.ControllerTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class ArticleQueryControllerTest extends ControllerTestSupport {

    @DisplayName("[통과] 게시글 버전 ID로 게시글을 조회한다.")
    @Test
    void article_query_controller_test_01() throws Exception {
        // given
        final Long articleVersionId = 1L;
        final LocalDateTime dateTime = LocalDateTime.of(2025, 1, 1, 1, 1, 1);
        final ArticleReadResponse response = new ArticleReadResponse(1L, 1L, "제목", "내용", false, false, dateTime);

        // stub
        when(articleQueryService.readArticleByArticleVersion(anyLong())).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/articles/versions/{articleVersionId}", articleVersionId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articleId").value(1))
                .andExpect(jsonPath("$.articleVersionId").value(1))
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(jsonPath("$.isNoEditing").value(false))
                .andExpect(jsonPath("$.isHiding").value(false))
                .andExpect(jsonPath("$.createdAt").value(dateTime.toString()))
                .andDo(print())
                .andDo(
                        document("readArticleByArticleVersion",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("articleVersionId").description("게시글 버전 ID")
                                )
                        )
                );

    }

    @DisplayName("[통과] 카테고리별 게시글 목록을 조회한다.")
    @Test
    void article_query_controller_test_02() throws Exception {
        // given
        final String category = "인물";
        final ArticleReadCategoryResponse response01 = new ArticleReadCategoryResponse(1L, "제목1");
        final ArticleReadCategoryResponse response02 = new ArticleReadCategoryResponse(2L, "제목2");
        final ArticleReadCategoryResponses responses =
                new ArticleReadCategoryResponses(List.of(response01, response02));

        // stub
        when(articleQueryService.readAllArticleByCategory(anyString())).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/articles/categories/{category}", category)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isArray())
                .andExpect(jsonPath("$.payload[0].articleVersionId").value(1))
                .andExpect(jsonPath("$.payload[0].title").value("제목1"))
                .andExpect(jsonPath("$.payload[1].articleVersionId").value(2))
                .andExpect(jsonPath("$.payload[1].title").value("제목2"))
                .andDo(print())
                .andDo(
                        document("readAllArticleByCategory",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("category").description("카테고리")
                                )
                        )
                );
    }

    @DisplayName("[통과] 최근 수정된 게시글 10개를 조회한다.")
    @Test
    void article_query_controller_test_03() throws Exception {
        // given
        final LocalDateTime dateTime01 = LocalDateTime.of(2025, 1, 1, 1, 1, 1);
        final LocalDateTime dateTime02 = LocalDateTime.of(2025, 1, 1, 1, 1, 2);

        final ArticleReadLatestUpdateResponse response01 =
                new ArticleReadLatestUpdateResponse(1L, "제목1", "인물", dateTime01);
        final ArticleReadLatestUpdateResponse response02 =
                new ArticleReadLatestUpdateResponse(2L, "제목2", "길드", dateTime02);
        final ArticleReadLatestUpdateResponses responses =
                new ArticleReadLatestUpdateResponses(List.of(response02, response01));

        // stub
        when(articleQueryService.readAllArticleByLatestUpdate()).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/articles/latest-update")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isArray())
                .andExpect(jsonPath("$.payload[0].articleVersionId").value(2))
                .andExpect(jsonPath("$.payload[0].title").value("제목2"))
                .andExpect(jsonPath("$.payload[0].category").value("길드"))
                .andExpect(jsonPath("$.payload[0].updatedAt").value(dateTime02.toString()))
                .andExpect(jsonPath("$.payload[1].articleVersionId").value(1))
                .andExpect(jsonPath("$.payload[1].title").value("제목1"))
                .andExpect(jsonPath("$.payload[1].category").value("인물"))
                .andExpect(jsonPath("$.payload[1].updatedAt").value(dateTime01.toString()))
                .andDo(print())
                .andDo(
                        document("readAllArticleByLatestUpdate",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @DisplayName("[통과] 게시글 번호로 게시글 버전 목록을 조회한다.")
    @Test
    void article_query_controller_test_05() throws Exception {
        // given
        final Long articleId = 1L;
        final LocalDateTime dateTime01 = LocalDateTime.of(2025, 1, 1, 1, 1, 1);
        final LocalDateTime dateTime02 = LocalDateTime.of(2025, 1, 1, 1, 1, 2);

        final ArticleVersionReadArticleResponse response01 =
                new ArticleVersionReadArticleResponse(1L, "작성자1", 1, 10, false, dateTime01);
        final ArticleVersionReadArticleResponse response02 =
                new ArticleVersionReadArticleResponse(2L, "작성자2", 2, 10, false, dateTime02);
        final ArticleVersionReadArticleResponses responses =
                new ArticleVersionReadArticleResponses("제목", List.of(response02, response01));

        // stub
        when(articleQueryService.readAllArticleVersionByArticle(anyLong())).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/articles/{articleId}/versions", articleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.payload").isArray())
                .andExpect(jsonPath("$.payload[0].articleVersionId").value(2))
                .andExpect(jsonPath("$.payload[0].nickname").value("작성자2"))
                .andExpect(jsonPath("$.payload[0].versionNumber").value(2))
                .andExpect(jsonPath("$.payload[0].size").value(10))
                .andExpect(jsonPath("$.payload[0].isHiding").value(false))
                .andExpect(jsonPath("$.payload[0].createdAt").value(dateTime02.toString()))
                .andExpect(jsonPath("$.payload[1].articleVersionId").value(1))
                .andExpect(jsonPath("$.payload[1].nickname").value("작성자1"))
                .andExpect(jsonPath("$.payload[1].versionNumber").value(1))
                .andExpect(jsonPath("$.payload[1].size").value(10))
                .andExpect(jsonPath("$.payload[1].isHiding").value(false))
                .andExpect(jsonPath("$.payload[1].createdAt").value(dateTime01.toString()))
                .andDo(print())
                .andDo(
                        document("readAllArticleVersionByArticle",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("articleId").description("게시글 ID")
                                )
                        )
                );
    }

    @DisplayName("[통과] 게시글 제목으로 게시글을 조회한다.")
    @Test
    void article_query_controller_test_06() throws Exception {
        // given
        final String title = "제목";
        final ArticleSearchResponse response01 = new ArticleSearchResponse(1L, "제목1", "인물");
        final ArticleSearchResponse response02 = new ArticleSearchResponse(2L, "제목2", "길드");
        final ArticleSearchResponses responses = new ArticleSearchResponses(List.of(response02, response01));

        // stub
        when(articleQueryService.searchArticleByTitle(anyString())).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/articles/search?title={title}", title))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isArray())
                .andExpect(jsonPath("$.payload[0].articleVersionId").value(2))
                .andExpect(jsonPath("$.payload[0].title").value("제목2"))
                .andExpect(jsonPath("$.payload[0].category").value("길드"))
                .andExpect(jsonPath("$.payload[1].articleVersionId").value(1))
                .andExpect(jsonPath("$.payload[1].title").value("제목1"))
                .andExpect(jsonPath("$.payload[1].category").value("인물"))
                .andDo(print())
                .andDo(
                        document("searchArticleByTitle",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("title").description("게시글 제목")
                                )
                        )
                );
    }
}
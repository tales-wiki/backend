package com.openmpy.taleswiki.article.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.request.ArticleUpdateRequest;
import com.openmpy.taleswiki.article.presentation.response.ArticleCreateResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllByCategoryResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllRecentEditsResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadByCategoryResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadByVersionResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadRecentEditsResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadVersionResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadVersionsResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleUpdateResponse;
import com.openmpy.taleswiki.support.ControllerTestSupport;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class ArticleControllerTest extends ControllerTestSupport {

    @DisplayName("[통과] 게시글을 작성한다.")
    @Test
    void article_controller_test_01() throws Exception {
        // given
        final ArticleCreateRequest request = new ArticleCreateRequest("제목", "닉네임", "인물", "내용");
        final ArticleCreateResponse response = new ArticleCreateResponse(1L, "제목", "닉네임", "인물", 1);
        final String body = objectMapper.writeValueAsString(request);

        // stub
        when(articleService.create(any(ArticleCreateRequest.class), any(HttpServletRequest.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.nickname").value("닉네임"))
                .andExpect(jsonPath("$.category").value("인물"))
                .andExpect(jsonPath("$.version").value("1"))
                .andDo(print())
                .andDo(
                        document("createArticle",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("title").description("제목"),
                                        fieldWithPath("nickname").description("작성자"),
                                        fieldWithPath("category").description("카테고리"),
                                        fieldWithPath("content").description("내용")
                                )
                        )
                );
    }

    @DisplayName("[통과] 게시글을 조회한다.")
    @Test
    void article_controller_test_02() throws Exception {
        // given
        final Long articleId = 1L;
        final LocalDateTime latestUpdatedAt = LocalDateTime.of(2025, 3, 30, 12, 0, 0);
        final ArticleReadResponse response = new ArticleReadResponse("제목", "닉네임", "내용", 1, latestUpdatedAt);

        // stub
        when(articleService.read(anyLong())).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/articles/{articleId}", articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.nickname").value("닉네임"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(jsonPath("$.version").value("1"))
                .andExpect(jsonPath("$.createdAt").value("2025-03-30T12:00:00"))
                .andDo(print())
                .andDo(
                        document("readArticle",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("articleId").description("게시글 ID")
                                )
                        )
                );
    }

    @DisplayName("[통과] 게시글 버전을 조회한다.")
    @Test
    void article_controller_test_03() throws Exception {
        // given
        final Long articleId = 1L;
        final ArticleReadVersionResponse response01 =
                new ArticleReadVersionResponse("초원", 1, 10, LocalDateTime.of(2025, 3, 29, 12, 0, 0));
        final ArticleReadVersionResponse response02 =
                new ArticleReadVersionResponse("밍밍", 2, 20, LocalDateTime.of(2025, 3, 30, 12, 0, 0));
        final ArticleReadVersionsResponse response =
                new ArticleReadVersionsResponse("제목입니다.", List.of(response01, response02));

        // stub
        when(articleService.readWithVersions(anyLong())).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/articles/{articleId}/versions", articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목입니다."))
                .andExpect(jsonPath("$.responses").isArray())
                .andExpect(jsonPath("$.responses[0].nickname").value("초원"))
                .andExpect(jsonPath("$.responses[0].version").value("1"))
                .andExpect(jsonPath("$.responses[0].size").value("10"))
                .andExpect(jsonPath("$.responses[0].createdAt").value("2025-03-29T12:00:00"))
                .andExpect(jsonPath("$.responses[1].nickname").value("밍밍"))
                .andExpect(jsonPath("$.responses[1].version").value("2"))
                .andExpect(jsonPath("$.responses[1].size").value("20"))
                .andExpect(jsonPath("$.responses[1].createdAt").value("2025-03-30T12:00:00"))
                .andDo(print())
                .andDo(
                        document("readArticleWithVersion",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("articleId").description("게시글 ID")
                                )
                        )
                )
        ;
    }

    @DisplayName("[통과] 게시글을 버전으로 조회한다.")
    @Test
    void article_controller_test_04() throws Exception {
        // given
        final Long articleId = 1L;
        final int version = 1;

        final LocalDateTime latestUpdatedAt = LocalDateTime.of(2025, 3, 30, 12, 0, 0);
        final ArticleReadByVersionResponse response =
                new ArticleReadByVersionResponse("제목", "닉네임", "내용", latestUpdatedAt);

        // stub
        when(articleService.readByVersion(anyLong(), anyInt())).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/articles/{articleId}/versions/{version}", articleId, version)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.nickname").value("닉네임"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(jsonPath("$.createdAt").value("2025-03-30T12:00:00"))
                .andDo(print())
                .andDo(
                        document("readArticleByVersion",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("articleId").description("게시글 ID"),
                                        parameterWithName("version").description("게시글 버전")
                                )
                        )
                );
    }

    @DisplayName("[통과] 게시글을 수정한다.")
    @Test
    void article_controller_test_05() throws Exception {
        // given
        final Long articleId = 1L;

        final ArticleUpdateRequest request = new ArticleUpdateRequest("제목", "닉네임", "내용");
        final ArticleUpdateResponse response =
                new ArticleUpdateResponse(articleId, "수정된 제목", "수정된 닉네임", "수정된 내용", 2);
        final String body = objectMapper.writeValueAsString(request);

        // stub
        when(articleService.update(anyLong(), any(ArticleUpdateRequest.class), any(HttpServletRequest.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(put("/api/articles/{articleId}", articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("수정된 제목"))
                .andExpect(jsonPath("$.nickname").value("수정된 닉네임"))
                .andExpect(jsonPath("$.version").value("2"))
                .andDo(print())
                .andDo(
                        document("updateArticle",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("articleId").description("게시글 ID")
                                ),
                                requestFields(
                                        fieldWithPath("title").description("제목"),
                                        fieldWithPath("nickname").description("작성자"),
                                        fieldWithPath("content").description("내용")
                                )
                        )
                );
    }

    @DisplayName("[통과] 게시글을 삭제한다.")
    @Test
    void article_controller_test_06() throws Exception {
        // given
        final Long articleId = 1L;

        // stub
        doNothing().when(articleService).delete(anyLong());

        // when & then
        mockMvc.perform(delete("/api/articles/{articleId}", articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(
                        document("deleteArticle",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("articleId").description("게시글 ID")
                                )
                        )
                );
    }

    @DisplayName("[통과] 카테고리에 해당하는 게시글 전체를 조회한다.")
    @Test
    void article_controller_test_07() throws Exception {
        // given
        final ArticleReadByCategoryResponse response01 = new ArticleReadByCategoryResponse(1L, "제목01");
        final ArticleReadByCategoryResponse response02 = new ArticleReadByCategoryResponse(2L, "제목02");
        final List<ArticleReadByCategoryResponse> responses = List.of(response01, response02);
        final ArticleReadAllByCategoryResponse response = new ArticleReadAllByCategoryResponse(2, responses);

        // stub
        when(articleService.readAllByCategory(any(ArticleCategory.class))).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/articles/categories/{category}", ArticleCategory.PERSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value("2"))
                .andExpect(jsonPath("$.responses").isArray())
                .andExpect(jsonPath("$.responses[0].id").value("1"))
                .andExpect(jsonPath("$.responses[0].title").value("제목01"))
                .andExpect(jsonPath("$.responses[1].id").value("2"))
                .andExpect(jsonPath("$.responses[1].title").value("제목02"))
                .andDo(print())
                .andDo(
                        document("readArticlesByCategory",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
        ;
    }

    @DisplayName("[통과] 최근 편집된 게시글 10개를 수정 날짜 기준으로 내림차순 한다.")
    @Test
    void article_controller_test_08() throws Exception {
        // given
        final List<ArticleReadRecentEditsResponse> list = new ArrayList<>();

        for (int i = 11; i > 1; i--) {
            final String title = String.format("제목%02d", i);
            final LocalDateTime dateTime = LocalDateTime.of(2025, 4, i, 12, 0, 1);
            final ArticleReadRecentEditsResponse response =
                    new ArticleReadRecentEditsResponse((long) i, title, "PERSON", dateTime);

            list.add(response);
        }

        final ArticleReadAllRecentEditsResponse response = new ArticleReadAllRecentEditsResponse(list);

        // stub
        when(articleService.readAllRecentEdits()).thenReturn(response);

        // when & then
        final ResultActions resultActions = mockMvc.perform(get("/api/articles/recent-edits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responses").isArray());

        for (int i = 0; i < list.size(); i++) {
            resultActions
                    .andExpect(jsonPath("$.responses[" + i + "].id").value(list.get(i).id()))
                    .andExpect(jsonPath("$.responses[" + i + "].title").value(list.get(i).title()))
                    .andExpect(jsonPath("$.responses[" + i + "].category").value(list.get(i).category()))
                    .andExpect(jsonPath("$.responses[" + i + "].createdAt").value(list.get(i).createdAt().toString()));
        }

        resultActions
                .andDo(print())
                .andDo(document("readArticlesOrderByRecentEdits",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}
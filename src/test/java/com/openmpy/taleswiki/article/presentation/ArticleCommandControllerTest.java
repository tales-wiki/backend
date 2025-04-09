package com.openmpy.taleswiki.article.presentation;

import static com.openmpy.taleswiki.support.Fixture.MEMBER_COOKIE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.request.ArticleUpdateRequest;
import com.openmpy.taleswiki.article.presentation.request.ArticleVersionReportRequest;
import com.openmpy.taleswiki.article.presentation.response.ArticleResponse;
import com.openmpy.taleswiki.support.ControllerTestSupport;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class ArticleCommandControllerTest extends ControllerTestSupport {

    @DisplayName("[통과] 게시글을 작성한다.")
    @Test
    void article_command_controller_test_01() throws Exception {
        // given
        final ArticleCreateRequest request = new ArticleCreateRequest("제목", "작성자", "인물", "내용");
        final ArticleResponse response = new ArticleResponse(1L);
        final String payload = objectMapper.writeValueAsString(request);

        // stub
        when(articleCommandService.createArticle(any(ArticleCreateRequest.class), any(HttpServletRequest.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                )
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.articleVersionId").value(1))
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

    @DisplayName("[통과] 게시글을 수정한다.")
    @Test
    void article_command_controller_test_02() throws Exception {
        // given
        final Long articleId = 1L;
        final ArticleUpdateRequest request = new ArticleUpdateRequest("작성자", "내용");
        final ArticleResponse response = new ArticleResponse(2L);
        final String payload = objectMapper.writeValueAsString(request);

        // stub
        when(articleCommandService
                .updateArticle(anyLong(), anyLong(), any(ArticleUpdateRequest.class), any(HttpServletRequest.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(put("/api/articles/{articleId}", articleId)
                        .cookie(MEMBER_COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.articleVersionId").value(2))
                .andDo(
                        document("updateArticle",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("nickname").description("작성자"),
                                        fieldWithPath("content").description("내용")
                                )
                        )
                );
    }

    @DisplayName("[통과] 게시글을 신고한다.")
    @Test
    void article_command_controller_test_03() throws Exception {
        // given
        final Long articleVersionId = 1L;
        final ArticleVersionReportRequest request = new ArticleVersionReportRequest("신고 내용");
        final String payload = objectMapper.writeValueAsString(request);

        // stub
        doNothing().when(articleCommandService)
                .reportArticleVersion(anyLong(), any(ArticleVersionReportRequest.class), any(HttpServletRequest.class));

        // when & then
        mockMvc.perform(post("/api/articles/versions/{articleVersionId}/report", articleVersionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                )
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(
                        document("reportArticleVersion",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("reportReason").description("신고 사유")
                                )
                        )
                );
    }
}
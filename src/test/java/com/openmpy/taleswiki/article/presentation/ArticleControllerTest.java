package com.openmpy.taleswiki.article.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openmpy.taleswiki.article.application.ArticleService;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.response.ArticleCreateResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadVersionResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadVersionsResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(controllers = ArticleController.class)
class ArticleControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArticleService articleService;

    @DisplayName("[통과] 게시글을 작성한다.")
    @Test
    void article_controller_test_01() throws Exception {
        // given
        final ArticleCreateRequest request = new ArticleCreateRequest("제목", "닉네임", "인물", "내용");
        final ArticleCreateResponse response = new ArticleCreateResponse(1L, "제목", "닉네임", "인물", 1);
        final String body = objectMapper.writeValueAsString(request);

        // stub
        when(articleService.create(any(ArticleCreateRequest.class))).thenReturn(response);

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
        final ArticleReadResponse response = new ArticleReadResponse("제목", "닉네임", "내용", 1L, latestUpdatedAt);

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
                .andExpect(jsonPath("$.latestVersion").value("1"))
                .andExpect(jsonPath("$.latestUpdatedAt").value("2025-03-30T12:00:00"))
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
                new ArticleReadVersionResponse("초원", 1, LocalDateTime.of(2025, 3, 29, 12, 0, 0));
        final ArticleReadVersionResponse response02 =
                new ArticleReadVersionResponse("밍밍", 2, LocalDateTime.of(2025, 3, 30, 12, 0, 0));
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
                .andExpect(jsonPath("$.responses[0].createdAt").value("2025-03-29T12:00:00"))
                .andExpect(jsonPath("$.responses[1].nickname").value("밍밍"))
                .andExpect(jsonPath("$.responses[1].version").value("2"))
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
}
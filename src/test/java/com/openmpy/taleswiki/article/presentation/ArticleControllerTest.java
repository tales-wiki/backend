package com.openmpy.taleswiki.article.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openmpy.taleswiki.article.application.ArticleService;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.response.ArticleCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.nickname").value("닉네임"))
                .andExpect(jsonPath("$.category").value("인물"))
                .andExpect(jsonPath("$.version").value("1"));
    }
}
package com.openmpy.taleswiki.report.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openmpy.taleswiki.report.presentation.request.ArticleReportRequest;
import com.openmpy.taleswiki.support.ControllerTestSupport;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class ReportControllerTest extends ControllerTestSupport {

    @DisplayName("[통과] 게시글을 신고한다.")
    @Test
    void report_controller_test_01() throws Exception {
        // given
        final long articleId = 1L;
        final ArticleReportRequest request = new ArticleReportRequest("신고 내용");
        final String body = objectMapper.writeValueAsString(request);

        // stub
        doNothing().when(reportService)
                .articleReport(anyLong(), any(ArticleReportRequest.class), any(HttpServletRequest.class));

        // when & then
        mockMvc.perform(post("/api/reports/articles/{id}", articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(
                        document("reportArticle",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("게시글 ID")
                                ),
                                requestFields(
                                        fieldWithPath("reason").description("신고 사유")
                                )
                        )
                );
    }
}
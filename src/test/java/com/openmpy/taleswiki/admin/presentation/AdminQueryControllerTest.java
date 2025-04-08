package com.openmpy.taleswiki.admin.presentation;

import static com.openmpy.taleswiki.support.Fixture.MEMBER_COOKIE;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionReportResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionReportResponses;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionResponses;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllMemberResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllMemberResponses;
import com.openmpy.taleswiki.support.ControllerTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class AdminQueryControllerTest extends ControllerTestSupport {

    @DisplayName("[통과] 회원 목록을 조회한다.")
    @Test
    void admin_query_controller_test_01() throws Exception {
        // given
        final LocalDateTime dateTime01 = LocalDateTime.of(2025, 1, 1, 1, 1, 1);
        final LocalDateTime dateTime02 = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

        final AdminReadAllMemberResponse response01 =
                new AdminReadAllMemberResponse(1L, "test1@test.com", "KAKAO", dateTime01);
        final AdminReadAllMemberResponse response02 =
                new AdminReadAllMemberResponse(2L, "test2@test.com", "GOOGLE", dateTime02);
        final AdminReadAllMemberResponses responses = new AdminReadAllMemberResponses(List.of(response01, response02));

        // stub
        when(adminQueryService.readAllMember(anyLong(), anyInt(), anyInt())).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/admin/members")
                        .cookie(MEMBER_COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isArray())
                .andExpect(jsonPath("$.payload[0].memberId").value(1))
                .andExpect(jsonPath("$.payload[0].email").value("test1@test.com"))
                .andExpect(jsonPath("$.payload[0].social").value("KAKAO"))
                .andExpect(jsonPath("$.payload[0].createdAt").value(dateTime01.toString()))
                .andExpect(jsonPath("$.payload[1].memberId").value(2))
                .andExpect(jsonPath("$.payload[1].email").value("test2@test.com"))
                .andExpect(jsonPath("$.payload[1].social").value("GOOGLE"))
                .andExpect(jsonPath("$.payload[1].createdAt").value(dateTime02.toString()))
                .andDo(print())
                .andDo(
                        document("readAllMember",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @DisplayName("[통과] 게시글 버전 목록을 조회한다.")
    @Test
    void admin_query_controller_test_02() throws Exception {
        // given
        final LocalDateTime dateTime01 = LocalDateTime.of(2025, 1, 1, 1, 1, 1);
        final LocalDateTime dateTime02 = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

        final AdminReadAllArticleVersionResponse response01 = new AdminReadAllArticleVersionResponse(
                1L,
                "제목1",
                "인물",
                "작성자1",
                "내용1",
                10,
                "127.0.0.1",
                false,
                dateTime01
        );
        final AdminReadAllArticleVersionResponse response02 = new AdminReadAllArticleVersionResponse(
                2L,
                "제목2",
                "길드",
                "작성자2",
                "내용2",
                10,
                "127.0.0.2",
                false,
                dateTime02
        );
        final AdminReadAllArticleVersionResponses responses =
                new AdminReadAllArticleVersionResponses(List.of(response01, response02));

        // stub
        when(adminQueryService.readAllArticleVersion(anyLong(), anyInt(), anyInt())).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/admin/articles/versions")
                        .cookie(MEMBER_COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isArray())
                .andExpect(jsonPath("$.payload[0].articleVersionId").value(1))
                .andExpect(jsonPath("$.payload[0].title").value("제목1"))
                .andExpect(jsonPath("$.payload[0].category").value("인물"))
                .andExpect(jsonPath("$.payload[0].nickname").value("작성자1"))
                .andExpect(jsonPath("$.payload[0].content").value("내용1"))
                .andExpect(jsonPath("$.payload[0].size").value(10))
                .andExpect(jsonPath("$.payload[0].ip").value("127.0.0.1"))
                .andExpect(jsonPath("$.payload[0].isHiding").value(false))
                .andExpect(jsonPath("$.payload[0].createdAt").value(dateTime01.toString()))
                .andExpect(jsonPath("$.payload[1].articleVersionId").value(2))
                .andExpect(jsonPath("$.payload[1].title").value("제목2"))
                .andExpect(jsonPath("$.payload[1].category").value("길드"))
                .andExpect(jsonPath("$.payload[1].nickname").value("작성자2"))
                .andExpect(jsonPath("$.payload[1].content").value("내용2"))
                .andExpect(jsonPath("$.payload[1].size").value(10))
                .andExpect(jsonPath("$.payload[1].ip").value("127.0.0.2"))
                .andExpect(jsonPath("$.payload[1].isHiding").value(false))
                .andExpect(jsonPath("$.payload[1].createdAt").value(dateTime02.toString()))
                .andDo(print())
                .andDo(
                        document("readAllArticleVersion",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @DisplayName("[통과] 게시글 버전 신고 목록을 조회한다.")
    @Test
    void admin_query_controller_test_03() throws Exception {
        // given
        final LocalDateTime dateTime01 = LocalDateTime.of(2025, 1, 1, 1, 1, 1);
        final LocalDateTime dateTime02 = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

        final AdminReadAllArticleVersionReportResponse response01 = new AdminReadAllArticleVersionReportResponse(
                1L,
                1L,
                "제목1",
                "인물",
                "작성자1",
                "내용1",
                "127.0.0.1",
                "신고 사유1",
                dateTime01
        );
        final AdminReadAllArticleVersionReportResponse response02 = new AdminReadAllArticleVersionReportResponse(
                2L,
                2L,
                "제목2",
                "길드",
                "작성자2",
                "내용2",
                "127.0.0.2",
                "신고 사유2",
                dateTime02
        );
        final AdminReadAllArticleVersionReportResponses responses =
                new AdminReadAllArticleVersionReportResponses(List.of(response01, response02));

        // stub
        when(adminQueryService.readAllArticleVersionReport(anyLong(), anyInt(), anyInt())).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/admin/articles/versions/reports")
                        .cookie(MEMBER_COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isArray())
                .andExpect(jsonPath("$.payload[0].articleVersionReportId").value(1))
                .andExpect(jsonPath("$.payload[0].articleVersionId").value(1))
                .andExpect(jsonPath("$.payload[0].title").value("제목1"))
                .andExpect(jsonPath("$.payload[0].category").value("인물"))
                .andExpect(jsonPath("$.payload[0].nickname").value("작성자1"))
                .andExpect(jsonPath("$.payload[0].content").value("내용1"))
                .andExpect(jsonPath("$.payload[0].ip").value("127.0.0.1"))
                .andExpect(jsonPath("$.payload[0].reportReason").value("신고 사유1"))
                .andExpect(jsonPath("$.payload[0].createdAt").value(dateTime01.toString()))
                .andExpect(jsonPath("$.payload[1].articleVersionReportId").value(2))
                .andExpect(jsonPath("$.payload[1].articleVersionId").value(2))
                .andExpect(jsonPath("$.payload[1].title").value("제목2"))
                .andExpect(jsonPath("$.payload[1].category").value("길드"))
                .andExpect(jsonPath("$.payload[1].nickname").value("작성자2"))
                .andExpect(jsonPath("$.payload[1].content").value("내용2"))
                .andExpect(jsonPath("$.payload[1].ip").value("127.0.0.2"))
                .andExpect(jsonPath("$.payload[1].reportReason").value("신고 사유2"))
                .andExpect(jsonPath("$.payload[1].createdAt").value(dateTime02.toString()))
                .andDo(print())
                .andDo(
                        document("readAllArticleVersionReport",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }
}
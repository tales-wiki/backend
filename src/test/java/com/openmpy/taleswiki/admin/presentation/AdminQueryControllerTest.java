package com.openmpy.taleswiki.admin.presentation;

import static com.openmpy.taleswiki.member.domain.MemberAuthority.ADMIN;
import static com.openmpy.taleswiki.member.domain.MemberAuthority.MEMBER;
import static com.openmpy.taleswiki.member.domain.MemberSocial.GOOGLE;
import static com.openmpy.taleswiki.member.domain.MemberSocial.KAKAO;
import static com.openmpy.taleswiki.support.Fixture.MEMBER_COOKIE;
import static org.mockito.ArgumentMatchers.anyInt;
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
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllBlockedIpResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllBlockedIpResponses;
import com.openmpy.taleswiki.common.presentation.response.PaginatedResponse;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.support.ControllerTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;

class AdminQueryControllerTest extends ControllerTestSupport {

    @DisplayName("[통과] 회원 목록을 조회한다.")
    @Test
    void admin_query_controller_test_01() throws Exception {
        // given
        final Member member01 = new Member(1L, "test1@test.com", KAKAO, MEMBER);
        final Member member02 = new Member(2L, "test2@test.com", GOOGLE, ADMIN);

        final Page<Member> members = new PageImpl<>(List.of(member02, member01), PageRequest.of(0, 10), 2);
        final PaginatedResponse<Member> response = PaginatedResponse.of(members);

        // stub
        when(adminQueryService.readAllMember(anyInt(), anyInt())).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/admin/members")
                        .cookie(MEMBER_COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(2))
                .andExpect(jsonPath("$.content[0].email").value("test2@test.com"))
                .andExpect(jsonPath("$.content[0].social").value("GOOGLE"))
                .andExpect(jsonPath("$.content[1].id").value(1))
                .andExpect(jsonPath("$.content[1].email").value("test1@test.com"))
                .andExpect(jsonPath("$.content[1].social").value("KAKAO"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.isFirst").value(true))
                .andExpect(jsonPath("$.isLast").value(true))
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
        final LocalDateTime dateTime02 = LocalDateTime.of(2025, 1, 1, 1, 1, 2);

        final AdminReadAllArticleVersionResponse response01 = new AdminReadAllArticleVersionResponse(
                1L,
                1L,
                "제목1",
                "인물",
                "작성자1",
                "내용1",
                10,
                "127.0.0.1",
                false,
                false,
                dateTime01
        );
        final AdminReadAllArticleVersionResponse response02 = new AdminReadAllArticleVersionResponse(
                2L,
                2L,
                "제목2",
                "길드",
                "작성자2",
                "내용2",
                10,
                "127.0.0.2",
                false,
                false,
                dateTime02
        );

        final Page<AdminReadAllArticleVersionResponse> responses =
                new PageImpl<>(List.of(response02, response01), PageRequest.of(0, 10), 2);
        final PaginatedResponse<AdminReadAllArticleVersionResponse> response = PaginatedResponse.of(responses);

        // stub
        when(adminQueryService.readAllArticleVersion(anyInt(), anyInt())).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/admin/articles/versions")
                        .cookie(MEMBER_COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].articleVersionId").value(2))
                .andExpect(jsonPath("$.content[0].articleId").value(2))
                .andExpect(jsonPath("$.content[0].title").value("제목2"))
                .andExpect(jsonPath("$.content[0].category").value("길드"))
                .andExpect(jsonPath("$.content[0].nickname").value("작성자2"))
                .andExpect(jsonPath("$.content[0].content").value("내용2"))
                .andExpect(jsonPath("$.content[0].size").value(10))
                .andExpect(jsonPath("$.content[0].ip").value("127.0.0.2"))
                .andExpect(jsonPath("$.content[0].isHiding").value(false))
                .andExpect(jsonPath("$.content[0].isNoEditing").value(false))
                .andExpect(jsonPath("$.content[0].createdAt").value(dateTime02.toString()))
                .andExpect(jsonPath("$.content[1].articleVersionId").value(1))
                .andExpect(jsonPath("$.content[1].articleId").value(1))
                .andExpect(jsonPath("$.content[1].title").value("제목1"))
                .andExpect(jsonPath("$.content[1].category").value("인물"))
                .andExpect(jsonPath("$.content[1].nickname").value("작성자1"))
                .andExpect(jsonPath("$.content[1].content").value("내용1"))
                .andExpect(jsonPath("$.content[1].size").value(10))
                .andExpect(jsonPath("$.content[1].ip").value("127.0.0.1"))
                .andExpect(jsonPath("$.content[1].isHiding").value(false))
                .andExpect(jsonPath("$.content[1].isNoEditing").value(false))
                .andExpect(jsonPath("$.content[1].createdAt").value(dateTime01.toString()))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.isFirst").value(true))
                .andExpect(jsonPath("$.isLast").value(true))
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
        final LocalDateTime dateTime02 = LocalDateTime.of(2025, 1, 1, 1, 1, 2);

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

        final Page<AdminReadAllArticleVersionReportResponse> responses =
                new PageImpl<>(List.of(response02, response01), PageRequest.of(0, 10), 2);
        final PaginatedResponse<AdminReadAllArticleVersionReportResponse> response = PaginatedResponse.of(responses);

        // stub
        when(adminQueryService.readAllArticleVersionReport(anyInt(), anyInt())).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/admin/articles/versions/reports")
                        .cookie(MEMBER_COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].articleVersionReportId").value(2))
                .andExpect(jsonPath("$.content[0].articleVersionId").value(2))
                .andExpect(jsonPath("$.content[0].title").value("제목2"))
                .andExpect(jsonPath("$.content[0].category").value("길드"))
                .andExpect(jsonPath("$.content[0].nickname").value("작성자2"))
                .andExpect(jsonPath("$.content[0].content").value("내용2"))
                .andExpect(jsonPath("$.content[0].ip").value("127.0.0.2"))
                .andExpect(jsonPath("$.content[0].reportReason").value("신고 사유2"))
                .andExpect(jsonPath("$.content[0].createdAt").value(dateTime02.toString()))
                .andExpect(jsonPath("$.content[1].articleVersionReportId").value(1))
                .andExpect(jsonPath("$.content[1].articleVersionId").value(1))
                .andExpect(jsonPath("$.content[1].title").value("제목1"))
                .andExpect(jsonPath("$.content[1].category").value("인물"))
                .andExpect(jsonPath("$.content[1].nickname").value("작성자1"))
                .andExpect(jsonPath("$.content[1].content").value("내용1"))
                .andExpect(jsonPath("$.content[1].ip").value("127.0.0.1"))
                .andExpect(jsonPath("$.content[1].reportReason").value("신고 사유1"))
                .andExpect(jsonPath("$.content[1].createdAt").value(dateTime01.toString()))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.isFirst").value(true))
                .andExpect(jsonPath("$.isLast").value(true))
                .andDo(print())
                .andDo(
                        document("readAllArticleVersionReport",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @DisplayName("[통과] 정지된 아이피 목록을 조회한다.")
    @Test
    void article_query_controller_test_07() throws Exception {
        // given
        final LocalDateTime dateTime01 = LocalDateTime.of(2025, 1, 1, 1, 1, 1);
        final LocalDateTime dateTime02 = LocalDateTime.of(2025, 1, 1, 1, 1, 2);

        final AdminReadAllBlockedIpResponse response01 =
                new AdminReadAllBlockedIpResponse(1L, "127.0.0.1", dateTime01);
        final AdminReadAllBlockedIpResponse response02 =
                new AdminReadAllBlockedIpResponse(2L, "127.0.0.2", dateTime02);
        final AdminReadAllBlockedIpResponses responses =
                new AdminReadAllBlockedIpResponses(List.of(response02, response01));

        // stub
        when(adminQueryService.readAllBlockedIp(anyInt(), anyInt())).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/admin/ip-block"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isArray())
                .andExpect(jsonPath("$.payload[0].blockedIpId").value(2))
                .andExpect(jsonPath("$.payload[0].ip").value("127.0.0.2"))
                .andExpect(jsonPath("$.payload[0].createdAt").value(dateTime02.toString()))
                .andExpect(jsonPath("$.payload[1].blockedIpId").value(1))
                .andExpect(jsonPath("$.payload[1].ip").value("127.0.0.1"))
                .andExpect(jsonPath("$.payload[1].createdAt").value(dateTime01.toString()))
                .andDo(print())
                .andDo(
                        document("readAllBlockedIp",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }
}
package com.openmpy.taleswiki.admin.presentation;

import static com.openmpy.taleswiki.support.Fixture.MEMBER_COOKIE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openmpy.taleswiki.admin.presentation.request.AdminBlockedIpRequest;
import com.openmpy.taleswiki.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class AdminCommandControllerTest extends ControllerTestSupport {

    @DisplayName("[통과] 게시글을 삭제한다.")
    @Test
    void admin_command_controller_test_01() throws Exception {
        // given
        final Long articleId = 1L;

        // stub
        doNothing().when(adminCommandService).deleteArticle(anyLong());

        // when & then
        mockMvc.perform(delete("/api/admin/articles/{articleId}", articleId)
                        .cookie(MEMBER_COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(
                        document("deleteArticle",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @DisplayName("[통과] 게시글 편집 모드를 수정한다.")
    @Test
    void admin_command_controller_test_02() throws Exception {
        // given
        final Long articleId = 1L;

        // stub
        doNothing().when(adminCommandService).toggleArticleEditMode(anyLong());

        // when & then
        mockMvc.perform(patch("/api/admin/articles/{articleId}/edit-mode", articleId)
                        .cookie(MEMBER_COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(
                        document("toggleArticleEditMode",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @DisplayName("[통과] 게시글 버전 숨김 모드를 수정한다.")
    @Test
    void admin_command_controller_test_03() throws Exception {
        // given
        final Long articleVersionId = 1L;

        // stub
        doNothing().when(adminCommandService).toggleArticleVersionHideMode(anyLong());

        // when & then
        mockMvc.perform(patch("/api/admin/articles/versions/{articleVersionId}/hide-mode", articleVersionId)
                        .cookie(MEMBER_COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(
                        document("toggleArticleVersionHideMode",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @DisplayName("[통과] 아이피 주소를 정지시킨다.")
    @Test
    void admin_command_controller_test_04() throws Exception {
        // given
        final AdminBlockedIpRequest request = new AdminBlockedIpRequest("127.0.0.1");
        final String payload = objectMapper.writeValueAsString(request);

        // stub
        doNothing().when(adminCommandService).addBlockedIp(any(AdminBlockedIpRequest.class));

        // when & then
        mockMvc.perform(post("/api/admin/ip-block")
                        .cookie(MEMBER_COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                )
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(
                        document("addBlockedIp",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("ip").description("아이피")
                                )
                        )
                );
    }

    @DisplayName("[통과] 아이피 주소 정지를 해제한다.")
    @Test
    void admin_command_controller_test_05() throws Exception {
        // given
        final AdminBlockedIpRequest request = new AdminBlockedIpRequest("127.0.0.1");
        final String payload = objectMapper.writeValueAsString(request);

        // stub
        doNothing().when(adminCommandService).deleteBlockedIp(any(AdminBlockedIpRequest.class));

        // when & then
        mockMvc.perform(delete("/api/admin/ip-block")
                        .cookie(MEMBER_COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                )
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(
                        document("deleteBlockedIp",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("ip").description("아이피")
                                )
                        )
                );
    }
}
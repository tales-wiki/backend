package com.openmpy.taleswiki.member.presentation;

import static com.openmpy.taleswiki.member.domain.MemberAuthority.MEMBER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openmpy.taleswiki.auth.jwt.JwtTokenProvider;
import com.openmpy.taleswiki.member.presentation.response.MemberLoginResponse;
import com.openmpy.taleswiki.support.ControllerTestSupport;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;

class MemberControllerTest extends ControllerTestSupport {

    @BeforeEach
    void setUp() {
        when(cookieProperties.maxAge()).thenReturn(Duration.ofDays(10L));
    }

    @DisplayName("[통과] 카카오 로그인을 한다.")
    @Test
    void member_controller_test_01() throws Exception {
        // given
        final MemberLoginResponse response = new MemberLoginResponse(1L, "test@test.com", MEMBER.name());
        final String token = "test-token";
        final ResponseCookie cookie = createCookie();

        // stub
        when(kakaoService.login(anyString())).thenReturn(response);
        when(memberService.generateToken(any(MemberLoginResponse.class))).thenReturn(token);

        // when & then
        mockMvc.perform(get("/api/members/login/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("code", "kakao-code")
                )
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, cookie.toString()))
                .andDo(print())
                .andDo(
                        document("loginKakao",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @DisplayName("[통과] 구글 로그인을 한다.")
    @Test
    void member_controller_test_02() throws Exception {
        // given
        final MemberLoginResponse response = new MemberLoginResponse(1L, "test@test.com", MEMBER.name());
        final String token = "test-token";
        final ResponseCookie cookie = createCookie();

        // stub
        when(googleService.login(anyString())).thenReturn(response);
        when(memberService.generateToken(any(MemberLoginResponse.class))).thenReturn(token);

        // when & then
        mockMvc.perform(get("/api/members/login/google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("code", "google-code")
                )
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, cookie.toString()))
                .andDo(print())
                .andDo(
                        document("loginGoogle",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    private ResponseCookie createCookie() {
        return ResponseCookie.from(JwtTokenProvider.ACCESS_TOKEN, "test-token")
                .httpOnly(cookieProperties.httpOnly())
                .secure(cookieProperties.secure())
                .domain(cookieProperties.domain())
                .path(cookieProperties.path())
                .sameSite(cookieProperties.sameSite())
                .maxAge(cookieProperties.maxAge())
                .build();
    }
}
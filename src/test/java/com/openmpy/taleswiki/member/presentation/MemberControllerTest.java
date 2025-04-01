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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openmpy.taleswiki.common.properties.CookieProperties;
import com.openmpy.taleswiki.member.application.GoogleService;
import com.openmpy.taleswiki.member.application.KakaoService;
import com.openmpy.taleswiki.member.application.MemberService;
import com.openmpy.taleswiki.member.presentation.response.MemberLoginResponse;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private KakaoService kakaoService;

    @MockitoBean
    private GoogleService googleService;

    @MockitoBean
    private CookieProperties cookieProperties;

    @BeforeEach
    void setUp() {
        when(cookieProperties.httpOnly()).thenReturn(true);
        when(cookieProperties.secure()).thenReturn(true);
        when(cookieProperties.domain()).thenReturn("localhost");
        when(cookieProperties.path()).thenReturn("/");
        when(cookieProperties.sameSite()).thenReturn("none");
        when(cookieProperties.maxAge()).thenReturn(Duration.ofDays(10L));
    }

    @DisplayName("[통과] 카카오 로그인을 한다.")
    @Test
    void member_controller_test_01() throws Exception {
        // given
        final MemberLoginResponse response = new MemberLoginResponse(1L, "test@test.com", MEMBER.name());
        final String token = "access-token";
        final ResponseCookie cookie = createCookie(token);

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
        final String token = "access-token";
        final ResponseCookie cookie = createCookie(token);

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

    private ResponseCookie createCookie(final String token) {
        return ResponseCookie.from("access-token", token)
                .httpOnly(cookieProperties.httpOnly())
                .secure(cookieProperties.secure())
                .domain(cookieProperties.domain())
                .path(cookieProperties.path())
                .sameSite(cookieProperties.sameSite())
                .maxAge(cookieProperties.maxAge())
                .build();
    }
}
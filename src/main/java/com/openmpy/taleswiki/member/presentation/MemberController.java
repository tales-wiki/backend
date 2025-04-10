package com.openmpy.taleswiki.member.presentation;

import com.openmpy.taleswiki.auth.annotation.Login;
import com.openmpy.taleswiki.auth.jwt.JwtTokenProvider;
import com.openmpy.taleswiki.common.properties.CookieProperties;
import com.openmpy.taleswiki.member.application.GoogleService;
import com.openmpy.taleswiki.member.application.KakaoService;
import com.openmpy.taleswiki.member.application.MemberService;
import com.openmpy.taleswiki.member.presentation.response.MemberLoginResponse;
import com.openmpy.taleswiki.member.presentation.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final CookieProperties cookieProperties;

    @GetMapping("/login/kakao")
    public ResponseEntity<Void> loginKakao(@RequestParam("code") final String code) {
        final MemberLoginResponse response = kakaoService.login(code);
        final String token = memberService.generateToken(response);
        final ResponseCookie cookie = createCookie(token);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @GetMapping("/login/google")
    public ResponseEntity<Void> loginGoogle(@RequestParam("code") final String code) {
        final MemberLoginResponse response = googleService.login(code);
        final String token = memberService.generateToken(response);
        final ResponseCookie cookie = createCookie(token);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout() {
        final ResponseCookie cookie = deleteCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> me(@Login final Long memberId) {
        final MemberResponse response = memberService.me(memberId);
        return ResponseEntity.ok().body(response);
    }

    private ResponseCookie createCookie(final String token) {
        return ResponseCookie.from(JwtTokenProvider.ACCESS_TOKEN, token)
                .httpOnly(cookieProperties.httpOnly())
                .secure(cookieProperties.secure())
                .domain(cookieProperties.domain())
                .path(cookieProperties.path())
                .sameSite(cookieProperties.sameSite())
                .maxAge(cookieProperties.maxAge())
                .build();
    }

    private ResponseCookie deleteCookie() {
        return ResponseCookie.from(JwtTokenProvider.ACCESS_TOKEN, "")
                .httpOnly(cookieProperties.httpOnly())
                .secure(cookieProperties.secure())
                .domain(cookieProperties.domain())
                .path(cookieProperties.path())
                .sameSite(cookieProperties.sameSite())
                .maxAge(0)
                .build();
    }
}

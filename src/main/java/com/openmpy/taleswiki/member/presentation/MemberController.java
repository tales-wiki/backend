package com.openmpy.taleswiki.member.presentation;

import com.openmpy.taleswiki.member.application.KakaoService;
import com.openmpy.taleswiki.member.presentation.response.MemberLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final KakaoService kakaoService;

    @GetMapping("/login/kakao")
    public ResponseEntity<MemberLoginResponse> loginKakao(@RequestParam("code") final String code) {
        final MemberLoginResponse response = kakaoService.login(code);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

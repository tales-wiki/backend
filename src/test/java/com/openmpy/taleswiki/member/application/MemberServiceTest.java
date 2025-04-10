package com.openmpy.taleswiki.member.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_MEMBER_AUTHORITY;
import static com.openmpy.taleswiki.member.domain.MemberAuthority.ADMIN;
import static com.openmpy.taleswiki.member.domain.MemberAuthority.MEMBER;
import static com.openmpy.taleswiki.member.domain.MemberSocial.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.member.domain.repository.MemberRepository;
import com.openmpy.taleswiki.member.presentation.response.MemberLoginResponse;
import com.openmpy.taleswiki.member.presentation.response.MemberResponse;
import com.openmpy.taleswiki.support.CustomServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@CustomServiceTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("[통과] 회원가입을 한다.")
    @Test
    void member_service_test_01() {
        // given
        final String email = "test@test.com";

        // when
        final MemberLoginResponse response = memberService.join(email, KAKAO);

        // then
        assertThat(response.email()).isEqualTo("test@test.com");
        assertThat(response.role()).isEqualTo(MEMBER.name());
    }

    @DisplayName("[통과] 이미 회원가입 되어있는 경우 로그인을 한다.")
    @Test
    void member_service_test_02() {
        // given
        final String email = "test@test.com";
        final Member member = Member.create(email, KAKAO);

        memberRepository.save(member);

        // when
        final MemberLoginResponse response = memberService.join(email, KAKAO);

        // then
        assertThat(response.email()).isEqualTo("test@test.com");
        assertThat(response.role()).isEqualTo(MEMBER.name());
    }

    @DisplayName("[통과] 회원가입 또는 로그인 시 토큰이 발행된다.")
    @Test
    void member_service_test_03() {
        // given
        final MemberLoginResponse response = memberService.join("test@test.com", KAKAO);

        // when
        final String token = memberService.generateToken(response);

        // then
        assertThat(token).isNotNull();
    }

    @DisplayName("[통과] 회원 엔티티를 조회한다.")
    @Test
    void member_service_test_04() {
        // given
        final String email = "test@test.com";
        final Member member = Member.create(email, KAKAO);

        final Member savedMember = memberRepository.save(member);

        // when
        final Member foundMember = memberService.getMember(savedMember.getId());

        // then
        assertThat(foundMember.getId()).isNotNull();
        assertThat(foundMember.getEmail()).isEqualTo("test@test.com");
    }

    @DisplayName("[통과] 회원 권한이 어드민이다.")
    @Test
    void member_service_test_05() {
        // given
        final Member member = Member.builder()
                .email("test@test.com")
                .social(KAKAO)
                .authority(ADMIN)
                .build();

        final Member savedMember = memberRepository.save(member);

        // when & then
        assertDoesNotThrow(() -> memberService.checkAdminMember(savedMember.getId()));
    }

    @DisplayName("[통과] 회원 정보를 조회한다.")
    @Test
    void member_service_test_06() {
        // given
        final Member member = Member.builder()
                .email("test@test.com")
                .social(KAKAO)
                .authority(ADMIN)
                .build();

        final Member savedMember = memberRepository.save(member);

        // when
        final MemberResponse response = memberService.me(savedMember.getId());

        // then
        assertThat(response.memberId()).isEqualTo(savedMember.getId());
        assertThat(response.email()).isEqualTo("test@test.com");
        assertThat(response.social()).isEqualTo("KAKAO");
        assertThat(response.authority()).isEqualTo("ADMIN");
    }

    @DisplayName("[예외] 회원 권한이 어드민이 아니다.")
    @Test
    void 예외_member_service_test_01() {
        // given
        final String email = "test@test.com";
        final Member member = Member.create(email, KAKAO);

        final Member savedMember = memberRepository.save(member);

        // when & then
        assertThatThrownBy(() -> memberService.checkAdminMember(savedMember.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_MEMBER_AUTHORITY.getMessage());
    }
}
package com.openmpy.taleswiki.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @DisplayName("[통과] 회원 객체 생성자를 검사한다.")
    @Test
    void member_test_01() {
        // given
        final long id = 1L;
        final String email = "test@test.com";
        final MemberSocial social = MemberSocial.KAKAO;
        final MemberAuthority authority = MemberAuthority.MEMBER;

        // when
        final Member member = new Member(id, email, social, authority);

        // then
        assertThat(member.getId()).isEqualTo(1L);
        assertThat(member.getEmail()).isEqualTo("test@test.com");
        assertThat(member.getSocial()).isEqualTo(MemberSocial.KAKAO);
        assertThat(member.getAuthority()).isEqualTo(MemberAuthority.MEMBER);
    }

    @DisplayName("[통과] 회원 객체를 생성한다.")
    @Test
    void member_test_02() {
        // given
        final String email = "test@test.com";
        final MemberSocial social = MemberSocial.KAKAO;

        // when
        final Member member = Member.create(email, social);

        // then
        assertThat(member.getEmail()).isEqualTo("test@test.com");
        assertThat(member.getSocial()).isEqualTo(MemberSocial.KAKAO);
        assertThat(member.getAuthority()).isEqualTo(MemberAuthority.MEMBER);
    }
}
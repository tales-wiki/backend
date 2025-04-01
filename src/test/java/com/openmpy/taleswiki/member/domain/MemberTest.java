package com.openmpy.taleswiki.member.domain;

import static com.openmpy.taleswiki.member.domain.MemberAuthority.MEMBER;
import static com.openmpy.taleswiki.member.domain.MemberSocial.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @DisplayName("[통과] 회원 객체가 정상적으로 생성된다.")
    @Test
    void member_test_01() {
        // given
        final String email = "test@test.com";

        // when
        final Member member = new Member(email, KAKAO, MEMBER);

        // then
        assertThat(member.getEmail()).isEqualTo("test@test.com");
        assertThat(member.getSocial()).isEqualTo(KAKAO);
        assertThat(member.getAuthority()).isEqualTo(MEMBER);
    }

    @DisplayName("[통과] 회원 객체를 생성한다.")
    @Test
    void member_test_02() {
        // given
        final String email = "test@test.com";

        // when
        final Member member = Member.create(email, KAKAO);

        // then
        assertThat(member.getEmail()).isEqualTo("test@test.com");
        assertThat(member.getSocial()).isEqualTo(KAKAO);
        assertThat(member.getAuthority()).isEqualTo(MEMBER);
    }
}
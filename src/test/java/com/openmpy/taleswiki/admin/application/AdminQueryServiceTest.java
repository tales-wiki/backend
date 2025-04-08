package com.openmpy.taleswiki.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllMemberResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllMemberResponses;
import com.openmpy.taleswiki.member.application.MemberService;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.member.domain.MemberSocial;
import com.openmpy.taleswiki.member.domain.repository.MemberRepository;
import com.openmpy.taleswiki.support.CustomServiceTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@CustomServiceTest
class AdminQueryServiceTest {

    @Autowired
    private AdminQueryService adminQueryService;

    @Autowired
    private MemberRepository memberRepository;

    @MockitoBean
    private MemberService memberService;

    @DisplayName("[통과] 모든 회원 목록을 페이지 형식으로 조회한다.")
    @Test
    void admin_query_service_test_01() {
        // given
        for (int i = 0; i < 20; i++) {
            final Member member = Member.create(i + "test@test.com", MemberSocial.KAKAO);
            memberRepository.save(member);
        }

        // stub
        when(memberService.getMember(anyLong())).thenReturn(any(Member.class));

        // when
        final AdminReadAllMemberResponses responses = adminQueryService.readAllMember(1L, 0, 10);

        // then
        final List<AdminReadAllMemberResponse> payload = responses.payload();

        assertThat(payload).hasSize(10);
        assertThat(payload.getFirst().email()).isEqualTo("0test@test.com");
        assertThat(payload.getLast().email()).isEqualTo("9test@test.com");
    }
}
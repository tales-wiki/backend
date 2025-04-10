package com.openmpy.taleswiki.member.presentation.response;

import com.openmpy.taleswiki.member.domain.Member;

public record MemberResponse(
        Long memberId,
        String email,
        String social,
        String authority
) {

    public static MemberResponse of(final Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getSocial().name(),
                member.getAuthority().name()
        );
    }
}

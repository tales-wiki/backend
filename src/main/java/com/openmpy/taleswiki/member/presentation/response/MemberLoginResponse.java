package com.openmpy.taleswiki.member.presentation.response;

import com.openmpy.taleswiki.member.domain.Member;

public record MemberLoginResponse(
        Long id,
        String email
) {

    public static MemberLoginResponse of(final Member member) {
        return new MemberLoginResponse(member.getId(), member.getEmail());
    }
}

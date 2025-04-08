package com.openmpy.taleswiki.admin.presentation.response;

import com.openmpy.taleswiki.member.domain.Member;
import java.util.List;

public record AdminReadAllMemberResponses(
        List<AdminReadAllMemberResponse> payload
) {

    public static AdminReadAllMemberResponses of(final List<Member> members) {
        final List<AdminReadAllMemberResponse> responses = members.stream()
                .map(it -> new AdminReadAllMemberResponse(
                        it.getId(),
                        it.getEmail(),
                        it.getSocial().name(),
                        it.getCreatedAt())
                )
                .toList();

        return new AdminReadAllMemberResponses(responses);
    }
}

package com.openmpy.taleswiki.admin.presentation.response;

import java.time.LocalDateTime;

public record AdminReadAllMemberResponse(
        Long memberId,
        String email,
        String social,
        LocalDateTime createdAt
) {
}

package com.openmpy.taleswiki.admin.presentation.response;

import java.time.LocalDateTime;

public record AdminReadAllBlockedIpResponse(
        Long blockedIpId,
        String ip,
        LocalDateTime createdAt
) {
}

package com.openmpy.taleswiki.admin.presentation.response;

import com.openmpy.taleswiki.admin.domain.BlockedIp;
import java.util.List;

public record AdminReadAllBlockedIpResponses(
        List<AdminReadAllBlockedIpResponse> payload
) {

    public static AdminReadAllBlockedIpResponses of(final List<BlockedIp> blockedIps) {
        final List<AdminReadAllBlockedIpResponse> responses = blockedIps.stream()
                .map(it -> new AdminReadAllBlockedIpResponse(it.getId(), it.getIp(), it.getCreatedAt()))
                .toList();

        return new AdminReadAllBlockedIpResponses(responses);
    }
}

package com.openmpy.taleswiki.discord.application.request;

public record DiscordErrorMessageRequest(
        String errorMessage,
        String requestUri,
        String requestPayload,
        String ip
) {
}

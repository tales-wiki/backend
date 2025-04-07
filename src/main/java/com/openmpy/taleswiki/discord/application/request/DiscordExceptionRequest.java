package com.openmpy.taleswiki.discord.application.request;

public record DiscordExceptionRequest(
        String errorCode,
        String errorMessage,
        String requestUri,
        String requestPayload,
        String ip
) {

    private static final int PAYLOAD_MAX_LENGTH = 1000;

    public DiscordExceptionRequest {
        if (requestPayload != null && requestPayload.length() > PAYLOAD_MAX_LENGTH) {
            requestPayload = requestPayload.substring(0, PAYLOAD_MAX_LENGTH) + "...";
        }
    }
}

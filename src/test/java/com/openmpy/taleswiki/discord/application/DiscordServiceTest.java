package com.openmpy.taleswiki.discord.application;

import com.openmpy.taleswiki.discord.application.request.DiscordErrorMessageRequest;
import com.openmpy.taleswiki.support.ServiceTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.EnabledIf;

@EnabledIf(expression = "#{environment.acceptsProfiles('dev')}", loadContext = true)
class DiscordServiceTest extends ServiceTestSupport {

    @Autowired
    private DiscordService discordService;

    @DisplayName("[통과] 디스코드 채널로 에러 메세지를 보낸다.")
    @Test
    void discord_service_test_01() {
        final DiscordErrorMessageRequest messageRequest =
                new DiscordErrorMessageRequest("error-message", "request-uri", "request-payload", "ip");

        discordService.sendErrorMessage(messageRequest);
    }
}
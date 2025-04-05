package com.openmpy.taleswiki.discord.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.DISCORD_ERROR;
import static com.openmpy.taleswiki.discord.application.DiscordMessageType.WELCOME_MESSAGE;

import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.common.properties.DiscordProperties;
import com.openmpy.taleswiki.common.util.DateFormatterUtil;
import com.openmpy.taleswiki.member.domain.MemberSocial;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class DiscordService {

    private static final String DISCORD_CONTENT = "content";

    private final DiscordProperties discordProperties;
    private final RestClient restClient;

    @Async
    public void sendWelcomeMessage(final Long id, final String email, final MemberSocial social) {
        final String now = DateFormatterUtil.convert(LocalDateTime.now());
        final String message = String.format(WELCOME_MESSAGE.getValue(), id, email, social.name(), now);

        final Map<String, String> params = new HashMap<>();
        params.put(DISCORD_CONTENT, message);

        try {
            restClient.post()
                    .uri(discordProperties.infoUrl())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(params)
                    .retrieve()
                    .body(String.class);
        } catch (final Exception e) {
            throw new CustomException(DISCORD_ERROR);
        }
    }

    @Async
    public void sendWaringMessage(final String content) {
        final Map<String, String> params = new HashMap<>();
        params.put(DISCORD_CONTENT, content);

        try {
            restClient.post()
                    .uri(discordProperties.warningUrl())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(params)
                    .retrieve()
                    .body(String.class);
        } catch (final Exception e) {
            throw new CustomException(DISCORD_ERROR);
        }
    }

    @Async
    public void sendErrorMessage(final String content) {
        final Map<String, String> params = new HashMap<>();
        params.put(DISCORD_CONTENT, content);

        try {
            restClient.post()
                    .uri(discordProperties.errorUrl())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(params)
                    .retrieve()
                    .body(String.class);
        } catch (final Exception e) {
            throw new CustomException(DISCORD_ERROR);
        }
    }
}

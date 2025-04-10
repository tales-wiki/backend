package com.openmpy.taleswiki.discord.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.DISCORD_ERROR;

import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.common.properties.DiscordProperties;
import com.openmpy.taleswiki.discord.application.request.DiscordEmbeds;
import com.openmpy.taleswiki.discord.application.request.DiscordEmbeds.Embed;
import com.openmpy.taleswiki.discord.application.request.DiscordEmbeds.Field;
import com.openmpy.taleswiki.discord.application.request.DiscordErrorMessageRequest;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class DiscordService {

    private static final String EMBED_TITLE = "\uD83D\uDEA8 시스템 예외 발생";
    private static final String EMBED_DESCRIPTION = "**예외가 발생했습니다. 빠른 확인이 필요합니다.**";
    private static final int EMBED_COLOR = 16711680;

    private static final String FIELD_ERROR_MESSAGE = "에러 메세지";
    private static final String FIELD_REQUEST_URI = "요청 URI";
    private static final String FIELD_REQUEST_PAYLOAD = "요청 PAYLOAD";
    private static final String FIELD_IP = "아이피";

    private final DiscordProperties discordProperties;
    private final RestClient restClient;

    @Async
    public void sendErrorMessage(final DiscordErrorMessageRequest request) {
        final Field errorMessage = new Field(FIELD_ERROR_MESSAGE, request.errorMessage(), false);
        final Field requestUri = new Field(FIELD_REQUEST_URI, request.requestUri(), false);
        final Field requestPayload = new Field(FIELD_REQUEST_PAYLOAD, request.requestPayload(), false);
        final Field ip = new Field(FIELD_IP, request.ip(), false);
        final String timestamp = Instant.now().toString();

        final List<Field> fields = List.of(errorMessage, requestUri, requestPayload, ip);
        final Embed embed = new Embed(
                EMBED_TITLE,
                EMBED_DESCRIPTION,
                EMBED_COLOR,
                timestamp,
                fields
        );
        final DiscordEmbeds embeds = new DiscordEmbeds(List.of(embed));

        try {
            restClient.post()
                    .uri(discordProperties.errorUrl())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(embeds)
                    .retrieve()
                    .body(String.class);
        } catch (final Exception e) {
            throw new CustomException(DISCORD_ERROR);
        }
    }
}

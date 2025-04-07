package com.openmpy.taleswiki.discord.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.DISCORD_ERROR;

import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.common.properties.DiscordProperties;
import com.openmpy.taleswiki.discord.application.request.DiscordArticleReportRequest;
import com.openmpy.taleswiki.discord.application.request.DiscordExceptionRequest;
import com.openmpy.taleswiki.discord.application.request.DiscordSignupRequest;
import com.openmpy.taleswiki.discord.dto.DiscordMessage;
import com.openmpy.taleswiki.discord.dto.DiscordMessage.Embed;
import com.openmpy.taleswiki.discord.dto.DiscordMessage.Field;
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

    private static final int COLOR_GREEN = 0x1DDB16;
    private static final int COLOR_ORANGE = 0xFF5E00;
    private static final int COLOR_RED = 0xFF0000;
    private static final int COLOR_PURPLE = 0x5F00FF;

    private final DiscordProperties discordProperties;
    private final RestClient restClient;

    @Async
    public void sendSignupMessage(final DiscordSignupRequest request) {
        final Field memberId = new Field("회원 ID", request.id());
        final Field email = new Field("이메일", request.email());
        final Field social = new Field("소셜", request.social());

        final Embed embed = new Embed(
                List.of(memberId, email, social),
                COLOR_GREEN,
                Instant.now().toString()
        );
        final DiscordMessage payload = new DiscordMessage(List.of(embed));

        try {
            restClient.post()
                    .uri(discordProperties.signupUrl())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(payload)
                    .retrieve()
                    .body(String.class);
        } catch (final Exception e) {
            throw new CustomException(DISCORD_ERROR);
        }
    }

    @Async
    public void sendWaringMessage(final DiscordExceptionRequest request) {
        final Field errorCode = new Field("에러 코드", request.errorCode());
        final Field errorMessage = new Field("에러 메세지", request.errorMessage());
        final Field requestUri = new Field("요청 URI", request.requestUri());
        final Field requestPayload = new Field("요청 Payload", request.requestPayload());
        final Field ip = new Field("클라이언트 IP", request.ip());

        final Embed embed = new Embed(
                List.of(errorCode, errorMessage, requestUri, requestPayload, ip),
                COLOR_ORANGE,
                Instant.now().toString()
        );
        final DiscordMessage payload = new DiscordMessage(List.of(embed));

        try {
            restClient.post()
                    .uri(discordProperties.warningUrl())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(payload)
                    .retrieve()
                    .body(String.class);
        } catch (final Exception e) {
            throw new CustomException(DISCORD_ERROR);
        }
    }

    @Async
    public void sendErrorMessage(final DiscordExceptionRequest request) {
        final Field errorCode = new Field("에러 코드", request.errorCode());
        final Field errorMessage = new Field("에러 메세지", request.errorMessage());
        final Field requestUri = new Field("요청 URI", request.requestUri());
        final Field requestPayload = new Field("요청 Payload", request.requestPayload());
        final Field ip = new Field("클라이언트 IP", request.ip());

        final Embed embed = new Embed(
                List.of(errorCode, errorMessage, requestUri, requestPayload, ip),
                COLOR_RED,
                Instant.now().toString()
        );
        final DiscordMessage payload = new DiscordMessage(List.of(embed));

        try {
            restClient.post()
                    .uri(discordProperties.errorUrl())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(payload)
                    .retrieve()
                    .body(String.class);
        } catch (final Exception e) {
            throw new CustomException(DISCORD_ERROR);
        }
    }

    @Async
    public void sendArticleReportMessage(final DiscordArticleReportRequest request) {
        final Field articleId = new Field("게시글 ID", request.articleId());
        final Field articleVersionId = new Field("게시글 버전 ID", request.articleVersionId());
        final Field articleTitle = new Field("제목", request.title());
        final Field articleCategory = new Field("카테고리", request.category());
        final Field articleReportReason = new Field("신고내용", request.reportReason());

        final Embed embed = new Embed(
                List.of(articleId, articleVersionId, articleTitle, articleCategory, articleReportReason),
                COLOR_PURPLE,
                Instant.now().toString()
        );
        final DiscordMessage payload = new DiscordMessage(List.of(embed));

        try {
            restClient.post()
                    .uri(discordProperties.reportUrl())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(payload)
                    .retrieve()
                    .body(String.class);
        } catch (final Exception e) {
            throw new CustomException(DISCORD_ERROR);
        }
    }
}

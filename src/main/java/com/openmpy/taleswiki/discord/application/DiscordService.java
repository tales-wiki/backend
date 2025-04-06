package com.openmpy.taleswiki.discord.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.DISCORD_ERROR;
import static com.openmpy.taleswiki.discord.application.DiscordMessageType.ARTICLE_REPORT_MESSAGE;
import static com.openmpy.taleswiki.discord.application.DiscordMessageType.SIGNUP_MESSAGE;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.common.properties.DiscordProperties;
import com.openmpy.taleswiki.common.util.DateFormatterUtil;
import com.openmpy.taleswiki.member.domain.MemberSocial;
import com.openmpy.taleswiki.report.domain.ArticleReport;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
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
    public void sendSignupMessage(final Long id, final String email, final MemberSocial social) {
        final String now = DateFormatterUtil.convert(LocalDateTime.now());
        final String message = String.format(SIGNUP_MESSAGE.getValue(), id, email, social.name(), now);

        final Map<String, String> params = new HashMap<>();
        params.put(DISCORD_CONTENT, message);

        try {
            restClient.post()
                    .uri(discordProperties.signupUrl())
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

    @Async
    public void sendArticleReportMessage(final ArticleVersion articleVersion) {
        final String now = DateFormatterUtil.convert(LocalDateTime.now());
        final Article article = articleVersion.getArticle();
        final String message = String.format(
                ARTICLE_REPORT_MESSAGE.getValue(),
                article.getId(),
                article.getTitle(),
                article.getCategory().getValue(),
                articleVersion.getArticleReports().stream()
                        .map(ArticleReport::getReportReason)
                        .collect(Collectors.joining(", ")),
                now
        );

        final Map<String, String> params = new HashMap<>();
        params.put(DISCORD_CONTENT, message);

        try {
            restClient.post()
                    .uri(discordProperties.reportUrl())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(params)
                    .retrieve()
                    .body(String.class);
        } catch (final Exception e) {
            throw new CustomException(DISCORD_ERROR);
        }
    }
}

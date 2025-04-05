package com.openmpy.taleswiki.report.application;

import com.openmpy.taleswiki.article.application.ArticleService;
import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.common.exception.CustomErrorCode;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.common.util.IpAddressUtil;
import com.openmpy.taleswiki.discord.application.DiscordService;
import com.openmpy.taleswiki.report.domain.ArticleReport;
import com.openmpy.taleswiki.report.domain.repository.ArticleReportRepository;
import com.openmpy.taleswiki.report.presentation.request.ArticleReportRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReportService {

    private static final int MAX_ARTICLE_REPORT_COUNT = 10;

    private final ArticleReportRepository articleReportRepository;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;
    private final DiscordService discordService;

    @Transactional
    public void articleReport(
            final Long id,
            final ArticleReportRequest request,
            final HttpServletRequest servletRequest
    ) {
        final Article article = articleService.getArticle(id);
        final String ip = IpAddressUtil.getClientIp(servletRequest);

        if (articleReportRepository.existsByArticleAndIp_Value(article, ip)) {
            throw new CustomException(CustomErrorCode.ALREADY_REPORT_IP);
        }

        final ArticleReport articleReport = ArticleReport.report(ip, request.reason(), article);

        article.addReport(articleReport);
        articleRepository.save(article);

        // 숨김 처리
        if (articleReportRepository.countByArticle(article) >= MAX_ARTICLE_REPORT_COUNT) {
            discordService.sendArticleReportMessage(article);
            article.toggleHiding(true);
        }
    }
}

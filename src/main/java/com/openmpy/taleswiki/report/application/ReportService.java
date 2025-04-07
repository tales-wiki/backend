package com.openmpy.taleswiki.report.application;

import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionRepository;
import com.openmpy.taleswiki.common.exception.CustomErrorCode;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.common.util.IpAddressUtil;
import com.openmpy.taleswiki.discord.application.DiscordService;
import com.openmpy.taleswiki.discord.application.request.DiscordArticleReportRequest;
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
    private final ArticleVersionRepository articleVersionRepository;
    private final DiscordService discordService;

    @Transactional
    public void articleReport(
            final Long versionId,
            final ArticleReportRequest request,
            final HttpServletRequest servletRequest
    ) {
        final ArticleVersion articleVersion = articleVersionRepository.findById(versionId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ARTICLE_VERSION));
        final String ip = IpAddressUtil.getClientIp(servletRequest);

        if (articleReportRepository.existsByArticleVersionAndIp_Value(articleVersion, ip)) {
            throw new CustomException(CustomErrorCode.ALREADY_REPORT_IP);
        }

        final ArticleReport articleReport = ArticleReport.report(ip, request.reason(), articleVersion);

        articleVersion.addReport(articleReport);
        articleVersionRepository.save(articleVersion);

        // 숨김 처리
        if (articleReportRepository.countByArticleVersion(articleVersion) >= MAX_ARTICLE_REPORT_COUNT) {
            final DiscordArticleReportRequest reportRequest = DiscordArticleReportRequest.of(articleVersion);
            discordService.sendArticleReportMessage(reportRequest);
            articleVersion.toggleHiding(true);
        }
    }
}

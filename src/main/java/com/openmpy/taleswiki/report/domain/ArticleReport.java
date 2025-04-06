package com.openmpy.taleswiki.report.domain;

import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.common.domain.BaseEntity;
import com.openmpy.taleswiki.common.domain.ClientIp;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ArticleReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "ip", nullable = false))
    private ClientIp ip;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "reason", nullable = false))
    private ReportReason reportReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_version_id")
    private ArticleVersion articleVersion;

    @Builder
    public ArticleReport(final String ip, final String reportReason, final ArticleVersion articleVersion) {
        this.ip = new ClientIp(ip);
        this.reportReason = new ReportReason(reportReason);
        this.articleVersion = articleVersion;
    }

    public static ArticleReport report(
            final String ip,
            final String reportReason,
            final ArticleVersion articleVersion
    ) {
        return new ArticleReport(ip, reportReason, articleVersion);
    }

    public String getIp() {
        return ip.getValue();
    }

    public String getReportReason() {
        return reportReason.getValue();
    }
}

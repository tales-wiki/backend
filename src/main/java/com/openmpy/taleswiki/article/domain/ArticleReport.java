package com.openmpy.taleswiki.article.domain;

import com.openmpy.taleswiki.common.domain.ClientIp;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArticleReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "report_reason", nullable = false))
    private ArticleReportReason reportReason;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "client_ip", nullable = false))
    private ClientIp ip;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_version_id")
    private ArticleVersion articleVersion;

    public ArticleReport(
            final Long id,
            final String reportReason,
            final String ip,
            final LocalDateTime createdAt,
            final ArticleVersion articleVersion
    ) {
        this.id = id;
        this.reportReason = new ArticleReportReason(reportReason);
        this.ip = new ClientIp(ip);
        this.createdAt = createdAt;
        this.articleVersion = articleVersion;
    }

    @Builder
    public ArticleReport(
            final String reportReason,
            final String ip,
            final LocalDateTime createdAt,
            final ArticleVersion articleVersion
    ) {
        this.reportReason = new ArticleReportReason(reportReason);
        this.ip = new ClientIp(ip);
        this.createdAt = createdAt;
        this.articleVersion = articleVersion;
    }

    public static ArticleReport create(
            final String reportReason,
            final String ip,
            final ArticleVersion articleVersion
    ) {
        return ArticleReport.builder()
                .reportReason(reportReason)
                .ip(ip)
                .createdAt(LocalDateTime.now())
                .articleVersion(articleVersion)
                .build();
    }

    public String getReportReason() {
        return reportReason.getValue();
    }

    public String getIp() {
        return ip.getValue();
    }
}

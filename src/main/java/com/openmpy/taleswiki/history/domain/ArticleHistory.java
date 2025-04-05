package com.openmpy.taleswiki.history.domain;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.common.domain.BaseEntity;
import com.openmpy.taleswiki.common.domain.ClientIp;
import com.openmpy.taleswiki.member.domain.Member;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ArticleHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleHistoryType articleHistoryType;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "ip", nullable = false))
    private ClientIp ip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    public ArticleHistory(
            final ArticleHistoryType articleHistoryType,
            final String ip,
            final Member member,
            final Article article
    ) {
        this.articleHistoryType = articleHistoryType;
        this.ip = new ClientIp(ip);
        this.member = member;
        this.article = article;
    }

    public static ArticleHistory saveByCreate(
            final String ip,
            final Member member,
            final Article article
    ) {
        return new ArticleHistory(ArticleHistoryType.CREATE, ip, member, article);
    }

    public static ArticleHistory saveByDelete(
            final String ip,
            final Member member,
            final Article article
    ) {
        return new ArticleHistory(ArticleHistoryType.DELETE, ip, member, article);
    }

    public String getIp() {
        return ip.getValue();
    }
}

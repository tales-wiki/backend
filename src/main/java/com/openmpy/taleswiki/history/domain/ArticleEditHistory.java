package com.openmpy.taleswiki.history.domain;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.common.domain.BaseEntity;
import com.openmpy.taleswiki.common.domain.ClientIp;
import com.openmpy.taleswiki.member.domain.Member;
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
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ArticleEditHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "ip", nullable = false))
    private ClientIp ip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_version_id")
    private ArticleVersion articleVersion;

    public ArticleEditHistory(
            final String ip,
            final Member member,
            final Article article,
            final ArticleVersion articleVersion
    ) {
        this.ip = new ClientIp(ip);
        this.member = member;
        this.article = article;
        this.articleVersion = articleVersion;
    }

    public static ArticleEditHistory create(
            final String ip,
            final Member member,
            final Article article,
            final ArticleVersion articleVersion
    ) {
        return new ArticleEditHistory(ip, member, article, articleVersion);
    }

    public String getIp() {
        return ip.getValue();
    }
}

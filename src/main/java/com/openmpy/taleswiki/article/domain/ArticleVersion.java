package com.openmpy.taleswiki.article.domain;

import com.openmpy.taleswiki.common.domain.BaseEntity;
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

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ArticleVersion extends BaseEntity {

    private static final int DEFAULT_ARTICLE_VERSION = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "nickname", nullable = false))
    private ArticleNickname nickname;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(columnDefinition = "TEXT", name = "content"))
    private ArticleContent content;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "version", nullable = false))
    private ArticleVersionNumber version;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "size", nullable = false))
    private ArticleSize size;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Builder
    public ArticleVersion(
            final String nickname,
            final String content,
            final int version,
            final int size,
            final Article article
    ) {
        this.nickname = new ArticleNickname(nickname);
        this.content = new ArticleContent(content);
        this.version = new ArticleVersionNumber(version);
        this.size = new ArticleSize(size);
        this.article = article;
    }

    public static ArticleVersion create(
            final String nickname,
            final String content,
            final int size,
            final Article article
    ) {
        return ArticleVersion.builder()
                .nickname(nickname)
                .content(content)
                .version(DEFAULT_ARTICLE_VERSION)
                .article(article)
                .size(size)
                .build();
    }

    public static ArticleVersion update(
            final String nickname,
            final String content,
            final int version,
            final int size,
            final Article article
    ) {
        return ArticleVersion.builder()
                .nickname(nickname)
                .content(content)
                .version(version)
                .size(size)
                .article(article)
                .build();
    }

    public String getNickname() {
        return nickname.getValue();
    }

    public String getContent() {
        return content.getValue();
    }

    public int getVersion() {
        return version.getValue();
    }

    public int getSize() {
        return size.getValue();
    }
}

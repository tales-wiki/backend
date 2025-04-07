package com.openmpy.taleswiki.article.domain;

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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ArticleVersion {

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
    @AttributeOverride(name = "value", column = @Column(name = "version_number", nullable = false))
    private ArticleVersionNumber versionNumber;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "size", nullable = false))
    private ArticleSize size;

    @Column(nullable = false)
    private boolean isHiding;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    public ArticleVersion(
            final Long id,
            final String nickname,
            final String content,
            final int versionNumber,
            final int size,
            final boolean isHiding,
            final LocalDateTime createdAt,
            final Article article
    ) {
        this.id = id;
        this.nickname = new ArticleNickname(nickname);
        this.content = new ArticleContent(content);
        this.versionNumber = new ArticleVersionNumber(versionNumber);
        this.size = new ArticleSize(size);
        this.isHiding = isHiding;
        this.createdAt = createdAt;
        this.article = article;
    }

    @Builder
    public ArticleVersion(
            final String nickname,
            final String content,
            final int versionNumber,
            final int size,
            final boolean isHiding,
            final LocalDateTime createdAt,
            final Article article
    ) {
        this.nickname = new ArticleNickname(nickname);
        this.content = new ArticleContent(content);
        this.versionNumber = new ArticleVersionNumber(versionNumber);
        this.size = new ArticleSize(size);
        this.isHiding = isHiding;
        this.createdAt = createdAt;
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
                .versionNumber(DEFAULT_ARTICLE_VERSION)
                .size(size)
                .isHiding(false)
                .createdAt(LocalDateTime.now())
                .article(article)
                .build();
    }

    public String getNickname() {
        return nickname.getValue();
    }

    public String getContent() {
        return content.getValue();
    }

    public int getVersionNumber() {
        return versionNumber.getValue();
    }

    public int getSize() {
        return size.getValue();
    }
}

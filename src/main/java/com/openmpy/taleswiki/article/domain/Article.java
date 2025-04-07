package com.openmpy.taleswiki.article.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SQLRestriction("deleted_at is null")
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "title", nullable = false))
    private ArticleTitle title;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ArticleCategory category;

    @Column
    private boolean isNoEditing;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(insertable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "latest_version_id")
    private ArticleVersion latestVersion;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ArticleVersion> versions = new ArrayList<>();

    public Article(
            final Long id,
            final String title,
            final ArticleCategory category,
            final boolean isNoEditing,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt,
            final LocalDateTime deletedAt,
            final ArticleVersion latestVersion
    ) {
        this.id = id;
        this.title = new ArticleTitle(title);
        this.category = category;
        this.isNoEditing = isNoEditing;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.latestVersion = latestVersion;
    }

    @Builder
    public Article(
            final String title,
            final ArticleCategory category,
            final boolean isNoEditing,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt,
            final LocalDateTime deletedAt,
            final ArticleVersion latestVersion
    ) {
        this.title = new ArticleTitle(title);
        this.category = category;
        this.isNoEditing = isNoEditing;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.latestVersion = latestVersion;
    }

    public static Article create(
            final String title,
            final ArticleCategory category
    ) {
        return Article.builder()
                .title(title)
                .category(category)
                .isNoEditing(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .build();
    }

    public void addVersion(final ArticleVersion articleVersion) {
        this.versions.add(articleVersion);
        this.latestVersion = articleVersion;
    }

    public String getTitle() {
        return title.getValue();
    }
}

package com.openmpy.taleswiki.article.domain;

import com.openmpy.taleswiki.common.domain.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "title", nullable = false))
    private ArticleTitle title;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ArticleCategory category;

    @Column(insertable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleVersion> versions = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "latest_version_id")
    private ArticleVersion latestVersion;

    @Builder
    public Article(
            final String title,
            final ArticleCategory category,
            final List<ArticleVersion> versions,
            final ArticleVersion latestVersion
    ) {
        this.title = new ArticleTitle(title);
        this.category = category;
        this.versions = versions;
        this.latestVersion = latestVersion;
    }

    public static Article create(final String title, final String category) {
        return Article.builder()
                .title(title)
                .category(ArticleCategory.of(category))
                .versions(new ArrayList<>())
                .latestVersion(null)
                .build();
    }

    public void addVersion(final ArticleVersion version) {
        versions.add(version);
        latestVersion = version;
    }

    public String getTitle() {
        return title.getValue();
    }
}

package com.openmpy.taleswiki.article.domain;

import com.openmpy.taleswiki.common.domain.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "nickname", nullable = false))
    private ArticleNickname nickname;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleVersion> versions = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lastest_version_id")
    private ArticleVersion latestVersion;

    @Builder
    public Article(
            final String title,
            final String nickname,
            final List<ArticleVersion> versions,
            final ArticleVersion latestVersion
    ) {
        this.title = new ArticleTitle(title);
        this.nickname = new ArticleNickname(nickname);
        this.versions = versions;
        this.latestVersion = latestVersion;
    }

    public void addVersion(final ArticleVersion version) {
        versions.add(version);
        latestVersion = version;
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getNickname() {
        return nickname.getValue();
    }
}

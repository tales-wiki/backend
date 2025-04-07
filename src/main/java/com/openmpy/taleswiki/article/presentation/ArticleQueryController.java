package com.openmpy.taleswiki.article.presentation;

import com.openmpy.taleswiki.article.application.ArticleQueryService;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadCategoryResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadLatestUpdateResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleSearchResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleVersionReadArticleResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/articles")
@RestController
public class ArticleQueryController {

    private final ArticleQueryService articleQueryService;

    @GetMapping("/versions/{articleVersionId}")
    public ResponseEntity<ArticleReadResponse> readArticleByArticleVersion(@PathVariable final Long articleVersionId) {
        final ArticleReadResponse response = articleQueryService.readArticleByArticleVersion(articleVersionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/{category}")
    public ResponseEntity<ArticleReadCategoryResponses> readAllArticleByCategory(@PathVariable final String category) {
        final ArticleReadCategoryResponses responses = articleQueryService.readAllArticleByCategory(category);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/latest-update")
    public ResponseEntity<ArticleReadLatestUpdateResponses> readAllArticleByLatestUpdate() {
        final ArticleReadLatestUpdateResponses responses = articleQueryService.readAllArticleByLatestUpdate();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{articleId}/versions")
    public ResponseEntity<ArticleVersionReadArticleResponses> readAllArticleVersionByArticle(
            @PathVariable final Long articleId
    ) {
        final ArticleVersionReadArticleResponses responses =
                articleQueryService.readAllArticleVersionByArticle(articleId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<ArticleSearchResponses> searchArticleByTitle(
            @RequestParam(value = "title") final String title
    ) {
        final ArticleSearchResponses responses = articleQueryService.searchArticleByTitle(title);
        return ResponseEntity.ok(responses);
    }
}

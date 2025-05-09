package com.openmpy.taleswiki.article.presentation;

import com.openmpy.taleswiki.article.application.ArticleCommandService;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.request.ArticleUpdateRequest;
import com.openmpy.taleswiki.article.presentation.request.ArticleVersionReportRequest;
import com.openmpy.taleswiki.article.presentation.response.ArticleResponse;
import com.openmpy.taleswiki.auth.annotation.Login;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/articles")
@RestController
public class ArticleCommandController {

    private final ArticleCommandService articleCommandService;

    @PostMapping
    public ResponseEntity<ArticleResponse> createArticle(
            @RequestBody @Valid final ArticleCreateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final ArticleResponse response = articleCommandService.createArticle(request, servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<ArticleResponse> updateArticle(
            @Login final Long memberId,
            @PathVariable final Long articleId,
            @RequestBody @Valid final ArticleUpdateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final ArticleResponse response =
                articleCommandService.updateArticle(memberId, articleId, request, servletRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/versions/{articleVersionId}/report")
    public ResponseEntity<Void> reportArticleVersion(
            @PathVariable final Long articleVersionId,
            @RequestBody @Valid final ArticleVersionReportRequest request,
            final HttpServletRequest servletRequest
    ) {
        articleCommandService.reportArticleVersion(articleVersionId, request, servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

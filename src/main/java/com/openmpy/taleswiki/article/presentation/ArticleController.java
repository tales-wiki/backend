package com.openmpy.taleswiki.article.presentation;

import com.openmpy.taleswiki.article.application.ArticleService;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.request.ArticleUpdateRequest;
import com.openmpy.taleswiki.article.presentation.response.ArticleCreateResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllByCategoryResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllRecentEditsResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllVersionsResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadByVersionResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleUpdateResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/articles")
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleCreateResponse> create(
            @RequestBody @Valid final ArticleCreateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final ArticleCreateResponse response = articleService.create(request, servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleReadResponse> read(@PathVariable final Long id) {
        final ArticleReadResponse response = articleService.read(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<ArticleReadAllVersionsResponse> readWithVersions(@PathVariable final Long id) {
        final ArticleReadAllVersionsResponse response = articleService.readAllVersions(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/versions/{version}")
    public ResponseEntity<ArticleReadByVersionResponse> readByVersion(
            @PathVariable final Long id,
            @PathVariable final int version
    ) {
        final ArticleReadByVersionResponse response = articleService.readByVersion(id, version);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/{category}")
    public ResponseEntity<ArticleReadAllByCategoryResponse> readAllByCategory(
            @PathVariable final ArticleCategory category
    ) {
        final ArticleReadAllByCategoryResponse response = articleService.readAllByCategory(category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent-edits")
    public ResponseEntity<ArticleReadAllRecentEditsResponse> readRecentEdits() {
        final ArticleReadAllRecentEditsResponse response = articleService.readAllRecentEdits();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleUpdateResponse> update(
            @PathVariable final Long id,
            @RequestBody @Valid final ArticleUpdateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final ArticleUpdateResponse response = articleService.update(id, request, servletRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

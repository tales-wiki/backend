package com.openmpy.taleswiki.article.presentation;

import com.openmpy.taleswiki.article.application.ArticleService;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.response.ArticleCreateResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadByVersionResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadVersionsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/articles")
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleCreateResponse> create(@RequestBody @Valid final ArticleCreateRequest request) {
        final ArticleCreateResponse response = articleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleReadResponse> read(@PathVariable final Long id) {
        final ArticleReadResponse response = articleService.read(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<ArticleReadVersionsResponse> readWithVersions(@PathVariable final Long id) {
        final ArticleReadVersionsResponse response = articleService.readWithVersions(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/versions/{version}")
    public ResponseEntity<ArticleReadByVersionResponse> readByVersion(
            @PathVariable final Long id,
            @PathVariable final Integer version
    ) {
        final ArticleReadByVersionResponse response = articleService.readByVersion(id, version);
        return ResponseEntity.ok(response);
    }
}

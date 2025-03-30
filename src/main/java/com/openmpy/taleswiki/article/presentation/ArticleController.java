package com.openmpy.taleswiki.article.presentation;

import com.openmpy.taleswiki.article.application.ArticleService;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.response.ArticleCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ArticleCreateResponse> create(@RequestBody @Valid ArticleCreateRequest request) {
        final ArticleCreateResponse response = articleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

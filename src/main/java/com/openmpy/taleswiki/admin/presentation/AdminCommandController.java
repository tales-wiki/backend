package com.openmpy.taleswiki.admin.presentation;

import com.openmpy.taleswiki.admin.application.AdminCommandService;
import com.openmpy.taleswiki.auth.annotation.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminCommandController {

    private final AdminCommandService adminCommandService;

    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<Void> deleteArticle(@Login final Long memberId, @PathVariable final Long articleId) {
        adminCommandService.deleteArticle(memberId, articleId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/articles/{articleId}/edit-mode")
    public ResponseEntity<Void> toggleArticleEditMode(@Login final Long memberId, @PathVariable final Long articleId) {
        adminCommandService.toggleArticleEditMode(memberId, articleId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/articles/versions/{articleVersionId}/hide-mode")
    public ResponseEntity<Void> toggleArticleVersionHideMode(
            @Login final Long memberId,
            @PathVariable final Long articleVersionId
    ) {
        adminCommandService.toggleArticleVersionHideMode(memberId, articleVersionId);
        return ResponseEntity.noContent().build();
    }
}

package com.openmpy.taleswiki.admin.presentation;

import com.openmpy.taleswiki.admin.application.AdminCommandService;
import com.openmpy.taleswiki.admin.presentation.request.AdminBlockedIpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminCommandController {

    private final AdminCommandService adminCommandService;

    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable final Long articleId) {
        adminCommandService.deleteArticle(articleId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/articles/{articleId}/edit-mode")
    public ResponseEntity<Void> toggleArticleEditMode(@PathVariable final Long articleId) {
        adminCommandService.toggleArticleEditMode(articleId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/articles/versions/{articleVersionId}/hide-mode")
    public ResponseEntity<Void> toggleArticleVersionHideMode(@PathVariable final Long articleVersionId) {
        adminCommandService.toggleArticleVersionHideMode(articleVersionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/articles/versions/{articleVersionId}")
    public ResponseEntity<Void> deleteArticleVersion(@PathVariable final Long articleVersionId) {
        adminCommandService.deleteArticleVersion(articleVersionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/ip-block")
    public ResponseEntity<Void> addBlockedIp(@RequestBody @Valid final AdminBlockedIpRequest request) {
        adminCommandService.addBlockedIp(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/ip-block")
    public ResponseEntity<Void> deleteBlockedIp(@RequestBody @Valid final AdminBlockedIpRequest request) {
        adminCommandService.deleteBlockedIp(request);
        return ResponseEntity.noContent().build();
    }
}

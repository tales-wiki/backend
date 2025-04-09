package com.openmpy.taleswiki.admin.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.ALREADY_BLOCKED_IP;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_BLOCKED_IP;

import com.openmpy.taleswiki.admin.domain.BlockedIp;
import com.openmpy.taleswiki.admin.domain.repository.BlockedIpRepository;
import com.openmpy.taleswiki.admin.presentation.request.AdminBlockedIpRequest;
import com.openmpy.taleswiki.article.application.ArticleQueryService;
import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.common.exception.CustomException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminCommandService {

    private final BlockedIpRepository blockedIpRepository;
    private final ArticleQueryService articleQueryService;

    @Transactional
    public void deleteArticle(final Long articleId) {
        final Article article = articleQueryService.getArticle(articleId);

        article.delete(LocalDateTime.now());
    }

    @Transactional
    public void toggleArticleEditMode(final Long articleId) {
        final Article article = articleQueryService.getArticle(articleId);

        article.toggleNoEditing(!article.isNoEditing());
    }

    @Transactional
    public void toggleArticleVersionHideMode(final Long articleVersionId) {
        final ArticleVersion articleVersion = articleQueryService.getArticleVersion(articleVersionId);

        articleVersion.toggleHiding(!articleVersion.isHiding());
    }

    @Transactional
    public void addBlockedIp(final AdminBlockedIpRequest request) {
        final String ip = request.ip();

        if (blockedIpRepository.existsByIp_Value(ip)) {
            throw new CustomException(ALREADY_BLOCKED_IP);
        }

        final BlockedIp blockedIp = BlockedIp.create(ip);
        blockedIpRepository.save(blockedIp);
    }

    @Transactional
    public void deleteBlockedIp(final AdminBlockedIpRequest request) {
        final String ip = request.ip();
        final BlockedIp blockedIp = blockedIpRepository.findByIp_Value(ip)
                .orElseThrow(() -> new CustomException(NOT_FOUND_BLOCKED_IP));

        blockedIpRepository.delete(blockedIp);
    }
}

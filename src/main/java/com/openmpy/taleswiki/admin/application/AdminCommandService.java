package com.openmpy.taleswiki.admin.application;

import com.openmpy.taleswiki.article.application.ArticleQueryService;
import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.member.application.MemberService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminCommandService {

    private final MemberService memberService;
    private final ArticleQueryService articleQueryService;

    @Transactional
    public void deleteArticle(final Long memberId, final Long articleId) {
        memberService.checkAdminMember(memberId);

        final Article article = articleQueryService.getArticle(articleId);

        article.delete(LocalDateTime.now());
    }

    @Transactional
    public void toggleArticleEditMode(final Long memberId, final Long articleId) {
        memberService.checkAdminMember(memberId);

        final Article article = articleQueryService.getArticle(articleId);

        article.toggleNoEditing(!article.isNoEditing());
    }
}

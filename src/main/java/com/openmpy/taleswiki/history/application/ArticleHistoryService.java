package com.openmpy.taleswiki.history.application;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.common.util.IpAddressUtil;
import com.openmpy.taleswiki.history.domain.ArticleEditHistory;
import com.openmpy.taleswiki.history.domain.ArticleHistory;
import com.openmpy.taleswiki.history.domain.repository.ArticleEditHistoryRepository;
import com.openmpy.taleswiki.history.domain.repository.ArticleHistoryRepository;
import com.openmpy.taleswiki.member.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleHistoryService {

    private final ArticleHistoryRepository articleHistoryRepository;
    private final ArticleEditHistoryRepository articleEditHistoryRepository;

    @Transactional
    public void saveByCreate(
            final Member member,
            final Article article,
            final HttpServletRequest servletRequest
    ) {
        final String ip = IpAddressUtil.getClientIp(servletRequest);
        final ArticleHistory articleHistory = ArticleHistory.saveByCreate(ip, member, article);

        articleHistoryRepository.save(articleHistory);
    }

    @Transactional
    public void saveByEdit(
            final Member member,
            final Article article,
            final ArticleVersion articleVersion,
            final HttpServletRequest servletRequest
    ) {
        final String ip = IpAddressUtil.getClientIp(servletRequest);
        final ArticleEditHistory articleEditHistory = ArticleEditHistory.create(ip, member, article, articleVersion);

        articleEditHistoryRepository.save(articleEditHistory);
    }
}

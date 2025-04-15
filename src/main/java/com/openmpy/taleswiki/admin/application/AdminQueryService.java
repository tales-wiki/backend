package com.openmpy.taleswiki.admin.application;

import com.openmpy.taleswiki.admin.domain.BlockedIp;
import com.openmpy.taleswiki.admin.domain.repository.BlockedIpRepository;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionReportResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllBlockedIpResponses;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllMemberResponse;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.ArticleVersionReport;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionReportRepository;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionRepository;
import com.openmpy.taleswiki.common.presentation.response.PaginatedResponse;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.member.domain.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminQueryService {

    private final MemberRepository memberRepository;
    private final ArticleVersionRepository articleVersionRepository;
    private final ArticleVersionReportRepository articleVersionReportRepository;
    private final BlockedIpRepository blockedIpRepository;

    @Transactional(readOnly = true)
    public PaginatedResponse<AdminReadAllMemberResponse> readAllMember(final int page, final int size) {
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        final Page<Member> pageResult = memberRepository.findAll(pageRequest);
        final Page<AdminReadAllMemberResponse> responses = pageResult.map(it ->
                new AdminReadAllMemberResponse(
                        it.getId(),
                        it.getEmail(),
                        it.getSocial().name(),
                        it.getCreatedAt()
                )
        );

        return PaginatedResponse.of(responses);
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<AdminReadAllArticleVersionResponse> readAllArticleVersion(final int page, final int size) {
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        final Page<ArticleVersion> pageResult = articleVersionRepository.findAllWithArticle(pageRequest);
        final Page<AdminReadAllArticleVersionResponse> responses = pageResult.map(it ->
                new AdminReadAllArticleVersionResponse(
                        it.getId(),
                        it.getArticle().getId(),
                        it.getArticle().getTitle(),
                        it.getArticle().getCategory().toString(),
                        it.getNickname(),
                        it.getContent(),
                        it.getSize(),
                        it.getIp(),
                        it.isHiding(),
                        it.getArticle().isNoEditing(),
                        it.getCreatedAt()
                )
        );

        return PaginatedResponse.of(responses);
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<AdminReadAllArticleVersionReportResponse> readAllArticleVersionReport(
            final int page,
            final int size
    ) {
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        final Page<ArticleVersionReport> pageResult = articleVersionReportRepository
                .findAllWithExistingArticle(pageRequest);
        final Page<AdminReadAllArticleVersionReportResponse> responses = pageResult.map(it ->
                new AdminReadAllArticleVersionReportResponse(
                        it.getId(),
                        it.getArticleVersion().getId(),
                        it.getArticleVersion().getArticle().getTitle(),
                        it.getArticleVersion().getArticle().getCategory().toString(),
                        it.getArticleVersion().getNickname(),
                        it.getArticleVersion().getContent(),
                        it.getIp(),
                        it.getReportReason(),
                        it.getCreatedAt()
                )
        );

        return PaginatedResponse.of(responses);
    }

    @Transactional(readOnly = true)
    public AdminReadAllBlockedIpResponses readAllBlockedIp(final int page, final int size) {
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        final Page<BlockedIp> blockedIpPage = blockedIpRepository.findAll(pageRequest);
        final List<BlockedIp> blockedIps = blockedIpPage.getContent();

        return AdminReadAllBlockedIpResponses.of(blockedIps);
    }
}

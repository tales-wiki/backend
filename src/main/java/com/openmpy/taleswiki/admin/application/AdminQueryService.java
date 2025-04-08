package com.openmpy.taleswiki.admin.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_MEMBER_AUTHORITY;
import static com.openmpy.taleswiki.member.domain.MemberAuthority.ADMIN;

import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionResponses;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllMemberResponses;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionRepository;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.member.application.MemberService;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.member.domain.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminQueryService {

    private final MemberRepository memberRepository;
    private final ArticleVersionRepository articleVersionRepository;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public AdminReadAllMemberResponses readAllMember(final Long memberId, final int page, final int size) {
        final Member member = memberService.getMember(memberId);

        if (!member.getAuthority().equals(ADMIN)) {
            throw new CustomException(INVALID_MEMBER_AUTHORITY);
        }

        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<Member> memberPage = memberRepository.findAll(pageRequest);
        final List<Member> members = memberPage.getContent();

        return AdminReadAllMemberResponses.of(members);
    }

    @Transactional(readOnly = true)
    public AdminReadAllArticleVersionResponses readAllArticleVersion(
            final Long memberId,
            final int page,
            final int size
    ) {
        final Member member = memberService.getMember(memberId);

        if (!member.getAuthority().equals(ADMIN)) {
            throw new CustomException(INVALID_MEMBER_AUTHORITY);
        }

        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<ArticleVersion> articleVersionPage = articleVersionRepository.findAll(pageRequest);
        final List<ArticleVersion> articleVersions = articleVersionPage.getContent();

        return AdminReadAllArticleVersionResponses.of(articleVersions);
    }
}

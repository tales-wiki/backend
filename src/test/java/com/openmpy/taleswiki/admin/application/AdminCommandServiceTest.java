package com.openmpy.taleswiki.admin.application;

import static com.openmpy.taleswiki.article.domain.ArticleCategory.PERSON;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.ALREADY_BLOCKED_IP;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_BLOCKED_IP;
import static com.openmpy.taleswiki.support.Fixture.ADMIN_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.openmpy.taleswiki.admin.domain.BlockedIp;
import com.openmpy.taleswiki.admin.domain.repository.BlockedIpRepository;
import com.openmpy.taleswiki.admin.presentation.request.AdminBlockedIpRequest;
import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.member.application.MemberService;
import com.openmpy.taleswiki.support.CustomServiceTest;
import com.openmpy.taleswiki.support.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@CustomServiceTest
class AdminCommandServiceTest {

    @Autowired
    private AdminCommandService adminCommandService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private BlockedIpRepository blockedIpRepository;

    @MockitoBean
    private MemberService memberService;

    @DisplayName("[통과] 게시글을 삭제한다.")
    @Test
    void admin_command_service_test_01() {
        // given
        final Article article = Fixture.createArticle("제목", PERSON);
        final Article savedArticle = articleRepository.save(article);

        // stub
        when(memberService.getMember(anyLong())).thenReturn(ADMIN_MEMBER);

        // when
        adminCommandService.deleteArticle(1L, savedArticle.getId());

        // then
        final long count = articleRepository.count();

        assertThat(count).isZero();
    }

    @DisplayName("[통과] 게시글 편집 모드를 수정한다.")
    @Test
    void admin_command_service_test_02() {
        // given
        final Article article = Fixture.createArticle("제목", PERSON);
        final Article savedArticle = articleRepository.save(article);

        // stub
        when(memberService.getMember(anyLong())).thenReturn(ADMIN_MEMBER);

        // when
        adminCommandService.toggleArticleEditMode(1L, savedArticle.getId());

        // then
        assertThat(savedArticle.isNoEditing()).isTrue();
    }

    @DisplayName("[통과] 게시글 버전 숨김 모드를 수정한다.")
    @Test
    void admin_command_service_test_03() {
        // given
        final Article article = Fixture.createArticleWithVersion("제목", PERSON);
        final Article savedArticle = articleRepository.save(article);
        final ArticleVersion articleVersion = savedArticle.getLatestVersion();

        // stub
        when(memberService.getMember(anyLong())).thenReturn(ADMIN_MEMBER);

        // when
        adminCommandService.toggleArticleVersionHideMode(1L, articleVersion.getId());

        // then
        assertThat(articleVersion.isHiding()).isTrue();
    }

    @DisplayName("[통과] 아이피를 정지시킨다.")
    @Test
    void admin_command_service_test_04() {
        // given
        final AdminBlockedIpRequest request = new AdminBlockedIpRequest("127.0.0.1");

        // stub
        when(memberService.getMember(anyLong())).thenReturn(ADMIN_MEMBER);

        // when
        adminCommandService.addBlockedIp(1L, request);

        // then
        final BlockedIp blockedIp = blockedIpRepository.findAll().getFirst();

        assertThat(blockedIp.getIp()).isEqualTo("127.0.0.1");
    }

    @DisplayName("[통과] 아이피 정지를 해제한다.")
    @Test
    void admin_command_service_test_05() {
        // given
        final AdminBlockedIpRequest request = new AdminBlockedIpRequest("127.0.0.1");

        final BlockedIp blockedIp = BlockedIp.create("127.0.0.1");
        blockedIpRepository.save(blockedIp);

        // stub
        when(memberService.getMember(anyLong())).thenReturn(ADMIN_MEMBER);

        // when
        adminCommandService.deleteBlockedIp(1L, request);

        // then
        final long count = blockedIpRepository.count();

        assertThat(count).isZero();
    }

    @DisplayName("[예외] 이미 정지된 아이피를 정지시킨다.")
    @Test
    void 예외_admin_command_service_test_01() {
        // given
        final AdminBlockedIpRequest request = new AdminBlockedIpRequest("127.0.0.1");

        final BlockedIp blockedIp = BlockedIp.create("127.0.0.1");
        blockedIpRepository.save(blockedIp);

        // stub
        when(memberService.getMember(anyLong())).thenReturn(ADMIN_MEMBER);

        // when & then
        assertThatThrownBy(() -> adminCommandService.addBlockedIp(1L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ALREADY_BLOCKED_IP.getMessage());
    }

    @DisplayName("[예외] 정지 되지 않은 아이피를 정지 해제한다.")
    @Test
    void 예외_admin_command_service_test_02() {
        // given
        final AdminBlockedIpRequest request = new AdminBlockedIpRequest("127.0.0.1");

        // stub
        when(memberService.getMember(anyLong())).thenReturn(ADMIN_MEMBER);

        // when & then
        assertThatThrownBy(() -> adminCommandService.deleteBlockedIp(1L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_FOUND_BLOCKED_IP.getMessage());
    }
}
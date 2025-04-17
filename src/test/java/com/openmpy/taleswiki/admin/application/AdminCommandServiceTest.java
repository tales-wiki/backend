package com.openmpy.taleswiki.admin.application;

import static com.openmpy.taleswiki.article.domain.ArticleCategory.RUNNER;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.ALREADY_BLOCKED_IP;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_BLOCKED_IP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.admin.domain.BlockedIp;
import com.openmpy.taleswiki.admin.domain.repository.BlockedIpRepository;
import com.openmpy.taleswiki.admin.presentation.request.AdminBlockedIpRequest;
import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.support.Fixture;
import com.openmpy.taleswiki.support.ServiceTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AdminCommandServiceTest extends ServiceTestSupport {

    @Autowired
    private AdminCommandService adminCommandService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private BlockedIpRepository blockedIpRepository;

    @DisplayName("[통과] 게시글을 삭제한다.")
    @Test
    void admin_command_service_test_01() {
        // given
        final Article article = Fixture.createArticle("제목", RUNNER);
        final Article savedArticle = articleRepository.save(article);

        // when
        adminCommandService.deleteArticle(savedArticle.getId());

        // then
        final long count = articleRepository.count();

        assertThat(count).isZero();
    }

    @DisplayName("[통과] 게시글 편집 모드를 수정한다.")
    @Test
    void admin_command_service_test_02() {
        // given
        final Article article = Fixture.createArticle("제목", RUNNER);
        final Article savedArticle = articleRepository.save(article);

        // when
        adminCommandService.toggleArticleEditMode(savedArticle.getId());

        // then
        assertThat(savedArticle.isNoEditing()).isTrue();
    }

    @DisplayName("[통과] 게시글 버전 숨김 모드를 수정한다.")
    @Test
    void admin_command_service_test_03() {
        // given
        final Article article = Fixture.createArticleWithVersion("제목", RUNNER);
        final Article savedArticle = articleRepository.save(article);
        final ArticleVersion articleVersion = savedArticle.getLatestVersion();

        // when
        adminCommandService.toggleArticleVersionHideMode(articleVersion.getId());

        // then
        assertThat(articleVersion.isHiding()).isTrue();
    }

    @DisplayName("[통과] 아이피를 정지시킨다.")
    @Test
    void admin_command_service_test_04() {
        // given
        final AdminBlockedIpRequest request = new AdminBlockedIpRequest("127.0.0.1");

        // when
        adminCommandService.addBlockedIp(request);

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

        // when
        adminCommandService.deleteBlockedIp(request);

        // then
        final long count = blockedIpRepository.count();

        assertThat(count).isZero();
    }

    @DisplayName("[통과] 게시글 버전을 삭제한다.")
    @Test
    void admin_command_service_test_06() {
        // given
        final Article article = Fixture.createArticleWithVersions("제목", RUNNER);
        final Article savedArticle = articleRepository.save(article);
        final ArticleVersion articleVersion = savedArticle.getLatestVersion();

        // when
        adminCommandService.deleteArticleVersion(articleVersion.getId());

        // then
        assertThat(articleVersion.getDeletedAt()).isNotNull();
    }

    @DisplayName("[예외] 이미 정지된 아이피를 정지시킨다.")
    @Test
    void 예외_admin_command_service_test_01() {
        // given
        final AdminBlockedIpRequest request = new AdminBlockedIpRequest("127.0.0.1");

        final BlockedIp blockedIp = BlockedIp.create("127.0.0.1");
        blockedIpRepository.save(blockedIp);

        // when & then
        assertThatThrownBy(() -> adminCommandService.addBlockedIp(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ALREADY_BLOCKED_IP.getMessage());
    }

    @DisplayName("[예외] 정지 되지 않은 아이피를 정지 해제한다.")
    @Test
    void 예외_admin_command_service_test_02() {
        // given
        final AdminBlockedIpRequest request = new AdminBlockedIpRequest("127.0.0.1");

        // when & then
        assertThatThrownBy(() -> adminCommandService.deleteBlockedIp(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_FOUND_BLOCKED_IP.getMessage());
    }

    @DisplayName("[예외] 게시글 버전이 한개일 경우 삭제할 수 없다.")
    @Test
    void 예외_admin_command_service_test_03() {
        // given
        final Article article = Fixture.createArticleWithVersion("제목", RUNNER);
        final Article savedArticle = articleRepository.save(article);
        final ArticleVersion articleVersion = savedArticle.getLatestVersion();

        // when & then
        assertThatThrownBy(() -> adminCommandService.deleteArticleVersion(articleVersion.getId()));
    }
}
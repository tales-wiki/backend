package com.openmpy.taleswiki.common.application;

import static com.openmpy.taleswiki.support.Fixture.mockServerHttpRequest;
import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.taleswiki.article.application.ArticleCommandService;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.support.EmbeddedRedisConfig;
import com.openmpy.taleswiki.support.ServiceTestSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(EmbeddedRedisConfig.class)
class RedisServiceTest extends ServiceTestSupport {

    @Autowired
    private ArticleCommandService articleCommandService;

    @Autowired
    private RedisService redisService;

    @DisplayName("[예외] 게시글 작성을 10명이 동시에 누른다.")
    @Test
    void 예외_redis_service_test_01() throws InterruptedException {
        // given
        final ArticleCreateRequest request = new ArticleCreateRequest("제목", "작성자", "runner", "내용");

        final int threadCount = 10;
        final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch latch = new CountDownLatch(threadCount);
        final List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<>());

        // when
        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    articleCommandService.createArticle(request, mockServerHttpRequest());
                } catch (final Throwable e) {
                    exceptions.add(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // then
        assertThat(exceptions).hasSize(9);
    }
}
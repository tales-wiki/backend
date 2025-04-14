package com.openmpy.taleswiki.common.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.taleswiki.support.EmbeddedRedisConfig;
import com.openmpy.taleswiki.support.ServiceTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(EmbeddedRedisConfig.class)
class RedisServiceTest extends ServiceTestSupport {

    @Autowired
    private RedisService redisService;

    @DisplayName("[통과] 레디스에 데이터를 저장한다.")
    @Test
    void redis_service_test_01() {
        // when
        redisService.save("key", "value");

        // then
        final Object value = redisService.get("key");

        assertThat((String) value).isEqualTo("value");
    }

    @DisplayName("[통과] 레디스에 저장된 데이터를 조회한다.")
    @Test
    void redis_service_test_02() {
        // given
        redisService.save("key", "value");

        // when
        final Object value = redisService.get("key");

        // then
        assertThat((String) value).isEqualTo("value");
    }

    @DisplayName("[통과] 레디스에 저장된 데이터를 삭제한다.")
    @Test
    void redis_service_test_03() {
        // given
        redisService.save("key", "value");

        // when
        redisService.delete("key");

        // then
        final Object value = redisService.get("key");

        assertThat(value).isNull();
    }
}
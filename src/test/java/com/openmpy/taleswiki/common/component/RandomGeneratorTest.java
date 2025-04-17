package com.openmpy.taleswiki.common.component;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RandomGeneratorTest {

    private final RandomGenerator randomGenerator = new RandomGenerator();

    @DisplayName("[통과] 랜덤 숫자를 반환한다. (1 ~ max)")
    @Test
    void random_generator_test_01() {
        // when
        final long generate = randomGenerator.generate(10);

        // then
        assertThat(generate).isBetween(1L, 10L);
    }
}
package com.openmpy.taleswiki.common.component;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class RandomGenerator {

    private final Random random = new Random();

    public long generate(final long max) {
        return 1L + random.nextLong(max); // 1 ~ max
    }
}

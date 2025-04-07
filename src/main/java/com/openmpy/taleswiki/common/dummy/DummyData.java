package com.openmpy.taleswiki.common.dummy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DummyData {

    @Profile("test")
    @Bean
    private CommandLineRunner init() {
        return null;
    }
}

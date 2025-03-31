package com.openmpy.taleswiki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class TalesWikiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalesWikiApplication.class, args);
    }

}

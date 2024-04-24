package com.reynolds.composer.v1.generatorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ComposerV1GeneratorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComposerV1GeneratorServiceApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

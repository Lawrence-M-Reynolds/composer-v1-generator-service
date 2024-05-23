package com.reynolds.composer.v1.generatorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest
@Testcontainers
public class GeneratorServiceLocalRunner {

    public static void main(String[] args) {
        SpringApplication.from(ComposerV1GeneratorServiceApplication::main)
                .with(LocalDevTestcontainersConfig.class)
                .run(args);
    }

    @TestConfiguration(proxyBeanMethods = false)
    static private class LocalDevTestcontainersConfig {
        @Bean
        @ServiceConnection
        public MongoDBContainer mongoDBContainer() {
            var mongoDBContainer = new MongoDBContainer("mongo:7.0.9");
            mongoDBContainer.setPortBindings(List.of("27017:27017"));
            return mongoDBContainer;
        }
    }
}

package com.reynolds.composer.v1.generatorservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;

@SpringBootApplication
@ComponentScan("com.reynolds.composer.v1")
@EntityScan("com.reynolds.composer.v1")
public class ComposerV1GeneratorServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(ComposerV1GeneratorServiceApplication.class, args);

        String mongodDbHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
        String mongodDbPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");
        logger.info("Connected to MongoDb: " + mongodDbHost + ":" + mongodDbPort);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // TODO: Remove if unnecessary.
//    @Autowired
//    MongoOperations mongoTemplate;
//
//    @EventListener(ContextRefreshedEvent.class)
//    public void initIndicesAfterStartup() {
//
//        MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext = mongoTemplate.getConverter().getMappingContext();
//        IndexResolver resolver = new MongoPersistentEntityIndexResolver(mappingContext);
//
//        IndexOperations indexOps = mongoTemplate.indexOps(CompositionVariation.class);
//        resolver.resolveIndexFor(CompositionVariation.class).forEach(e -> indexOps.ensureIndex(e));
//    }
}

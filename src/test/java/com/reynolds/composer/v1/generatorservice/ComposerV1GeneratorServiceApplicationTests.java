package com.reynolds.composer.v1.generatorservice;

import com.reynolds.composer.v1.api.core.composition.composition.Composition;
import com.reynolds.composer.v1.api.core.composition.composition.generated.CompositionVariation;
import com.reynolds.composer.v1.generatorservice.persistence.CompositionVariationRepository;
import com.reynolds.composer.v1.generatorservice.services.CompositionServiceIntegration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ComposerV1GeneratorServiceApplicationTests {

    @Container
    @ServiceConnection
    private static MongoDBContainer database = new MongoDBContainer("mongo:7.0.9");

    @MockBean
    private CompositionServiceIntegration compositionServiceIntegration;

    static {
        database.start();
    }

    @Autowired
    private WebTestClient client;

    @Autowired
    private CompositionVariationRepository repository;

    @BeforeEach
    void setupDb() {
//        repository.deleteAll().block();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void fullWorkflowTest() {
        // Processing a composition
        long originalCompositionId = 22;
        Composition composition = new Composition();
        composition.setId(originalCompositionId);

        Mockito.when(compositionServiceIntegration.getComposition(originalCompositionId)).thenReturn(Mono.just(composition));

        List<String> vartionIds = postForProcessing(originalCompositionId, HttpStatus.OK);
        System.out.println("vartionIds: " + vartionIds);
        Assertions.assertEquals(3, vartionIds.size());

        // Getting the composition Count for the original composition.
        int count = getGeneratedCountForComposition(originalCompositionId, HttpStatus.OK);
        Assertions.assertEquals(3, count);

        // Getting all compositions for the original composition
        List<CompositionVariation> compositionVariations = getGeneratedVariationsForComposition(originalCompositionId, HttpStatus.OK);
        Assertions.assertEquals(3, compositionVariations.size());
    }

    private List<CompositionVariation> getGeneratedVariationsForComposition(long originalCompositionId, HttpStatus expectedStatus) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/generator/getGeneratedVariations")
                        .pathSegment(Long.toString(originalCompositionId))
                        .build())
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBodyList(CompositionVariation.class)
                .returnResult()
                .getResponseBody();
    }

    private Integer getGeneratedCountForComposition(long originalCompositionId, HttpStatus expectedStatus) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/generator/getGeneratedCount")
                        .pathSegment(Long.toString(originalCompositionId))
                        .build())
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody(Integer.class)
                .returnResult()
                .getResponseBody();
    }

    private List<String> postForProcessing(long originalCompositionId, HttpStatus expectedStatus) {
        return client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/generator/process")
                        .queryParam("compositionId", Long.toString(originalCompositionId))
                        .build())
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody(new ParameterizedTypeReference<List<String>>() {})
                .returnResult()
                .getResponseBody();
    }
}

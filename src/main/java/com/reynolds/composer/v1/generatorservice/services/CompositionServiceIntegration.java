package com.reynolds.composer.v1.generatorservice.services;

import com.reynolds.composer.v1.api.core.composition.composition.Composition;
import com.reynolds.composer.v1.api.core.composition.composition.CompositionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.invoke.MethodHandles;

import static org.springframework.http.HttpMethod.GET;

@Component
public class CompositionServiceIntegration implements CompositionController {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final WebClient webClient;

    public CompositionServiceIntegration(WebClient.Builder webClient,
                                         @Value("${app.composition-service.host}") String serviceHost,
                                         @Value("${app.composition-service.port}") int servicePort) {

        String serviceUrl = "http://" + serviceHost + ":" + servicePort + "/compositions";
        this.webClient = webClient.baseUrl(serviceUrl).build();
    }

    @Override
    public Mono<Composition> getComposition(long compositionId) {
        return webClient.post().uri(uriBuilder -> uriBuilder
                        .pathSegment(Long.toString(compositionId))
                        .build())
                .retrieve()
                .bodyToMono(Composition.class);
    }

}

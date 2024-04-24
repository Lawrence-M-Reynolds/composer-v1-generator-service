package com.reynolds.composer.v1.generatorservice.services;

import com.reynolds.composer.v1.api.core.composition.composition.Composition;
import com.reynolds.composer.v1.api.core.composition.composition.CompositionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Component
public class CompositionServiceIntegration implements CompositionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String compostionServiceUrl;
    private RestTemplate restTemplate;

    public CompositionServiceIntegration(RestTemplate restTemplate,
                                         @Value("${app.composition-service.host}") String serviceHost,
                                         @Value("${app.composition-service.port}") int servicePort) {

        this.restTemplate = restTemplate;
        compostionServiceUrl = "http://" + serviceHost + ":" + servicePort + "/compositions";
    }

    @Override
    public ResponseEntity<Composition> getComposition(long compositionId) {
        String url = compostionServiceUrl + "/" + compositionId;
        return restTemplate.exchange(url, GET, null, Composition.class);
    }

}

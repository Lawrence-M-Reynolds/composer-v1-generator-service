package com.reynolds.composer.v1.generatorservice.controller;

import com.reynolds.composer.v1.api.core.composition.composition.Composition;
import com.reynolds.composer.v1.api.core.generator.generator.GeneratorController;
import com.reynolds.composer.v1.generatorservice.services.CompositionServiceIntegration;
import com.reynolds.composer.v1.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GenerationControllerImpl implements GeneratorController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CompositionServiceIntegration compositionServiceIntegration;

    public GenerationControllerImpl(CompositionServiceIntegration compositionServiceIntegration) {
        this.compositionServiceIntegration = compositionServiceIntegration;
    }


    @Override
    public ResponseEntity<Void> processComposition(long compositionId) throws IOException {
        logger.debug("Processing composition: {}", compositionId);
        Composition composition = compositionServiceIntegration.getComposition(compositionId).getBody();

        logger.debug("Composition: {}", composition);
        // TODO - initiate generation here.
        return ResponseEntity.noContent().build();
    }

}

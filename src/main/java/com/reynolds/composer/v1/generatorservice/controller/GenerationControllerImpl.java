package com.reynolds.composer.v1.generatorservice.controller;

import com.reynolds.composer.v1.api.core.generator.generator.GeneratorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GenerationControllerImpl implements GeneratorController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ResponseEntity<Void> processComposition(long compositionId) throws IOException {
        logger.debug("Processing composition " + compositionId);
        // TODO - initiate generation here.
        return ResponseEntity.noContent().build();
    }

}

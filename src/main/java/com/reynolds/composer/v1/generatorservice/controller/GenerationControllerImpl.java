package com.reynolds.composer.v1.generatorservice.controller;

import com.reynolds.composer.v1.api.core.composition.composition.Composition;
import com.reynolds.composer.v1.api.core.composition.composition.generated.CompositionVariation;
import com.reynolds.composer.v1.api.core.generator.generator.GeneratorController;
import com.reynolds.composer.v1.generatorservice.services.CompositionServiceIntegration;
import com.reynolds.composer.v1.generatorservice.services.api.GeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class GenerationControllerImpl implements GeneratorController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CompositionServiceIntegration compositionServiceIntegration;
    private final GeneratorService generatorService;

    public GenerationControllerImpl(CompositionServiceIntegration compositionServiceIntegration,
                                    GeneratorService generatorService) {
        this.compositionServiceIntegration = compositionServiceIntegration;
        this.generatorService = generatorService;
    }

    @Override
    public ResponseEntity<Void> processComposition(long compositionId) throws IOException {
        logger.debug("Processing composition: {}", compositionId);
        Optional<Composition> compositionOptional = Optional.ofNullable(compositionServiceIntegration.getComposition(compositionId).getBody());

        if (compositionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Composition composition = compositionOptional.get();
        logger.debug("Composition: {}", composition);

        List<CompositionVariation> variations = generatorService.createVariations(composition);

        logger.debug("CompositionVariations: {}", variations);

        return ResponseEntity.noContent().build();
    }

    @Override
    public int getGeneratedCountForComposition(long compositionId) throws IOException {
        logger.debug("Get generation count for composition: {}", compositionId);
        return generatorService.getCountOfGeneratedForComposition(compositionId);
    }

    @Override
    public List<CompositionVariation> getGeneratedVariationsForComposition(long compositionId) throws IOException {
        return generatorService.getVariationsForComposition(compositionId);
    }
}

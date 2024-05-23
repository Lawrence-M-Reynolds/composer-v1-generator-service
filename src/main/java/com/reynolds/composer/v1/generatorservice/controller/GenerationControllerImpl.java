package com.reynolds.composer.v1.generatorservice.controller;

import com.reynolds.composer.v1.api.core.composition.composition.Composition;
import com.reynolds.composer.v1.api.core.composition.composition.generated.CompositionVariation;
import com.reynolds.composer.v1.api.core.generator.generator.GeneratorController;
import com.reynolds.composer.v1.generatorservice.services.CompositionServiceIntegration;
import com.reynolds.composer.v1.generatorservice.services.api.GeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    /**
     * Returns a list of the ID's of generated variations. If an error occurs during the processing of the stream then
     * the exception is logged and a "ERROR" value is returned in the response flux stream. If the original composition
     * isn't found then a 404 http status is returned.
     * @param compositionId
     * @return
     * @throws IOException
     */
    @Override
    public Mono<List<String>> processComposition(long compositionId) throws IOException {
        return Mono.defer(() -> {
            logger.debug("Processing composition: {}", compositionId);
            Composition composition = Optional.ofNullable(compositionServiceIntegration.getComposition(compositionId).getBody())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            logger.debug("Composition: {}", composition);

            return generatorService.createVariations(composition)
                    .map(compositionVariation -> Optional.ofNullable(compositionVariation.getCompositionVariationId()).orElse("null"))
                    .collectList()
                    .onErrorMap(e -> {
                        logger.error("Exception during processing of composition into variations: {}", e.getMessage(), e);
                        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
                    });
        });
    }

    @Override
    public Mono<Integer> getGeneratedCountForComposition(long compositionId) throws IOException {
        logger.debug("Get generation count for composition: {}", compositionId);
        return generatorService.getCountOfGeneratedForComposition(compositionId);
    }

    @Override
    public Flux<CompositionVariation> getGeneratedVariationsForComposition(long compositionId) throws IOException {
        return generatorService.getVariationsForComposition(compositionId);
    }

}

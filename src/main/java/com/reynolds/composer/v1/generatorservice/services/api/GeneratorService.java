package com.reynolds.composer.v1.generatorservice.services.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reynolds.composer.v1.api.core.composition.composition.Composition;
import com.reynolds.composer.v1.api.core.composition.composition.generated.CompositionVariation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GeneratorService {

    Flux<CompositionVariation> createVariations(Composition composition);

    Mono<Integer> getCountOfGeneratedForComposition(long compositionId);

    Flux<CompositionVariation> getVariationsForComposition(long compositionId) throws JsonProcessingException;
}

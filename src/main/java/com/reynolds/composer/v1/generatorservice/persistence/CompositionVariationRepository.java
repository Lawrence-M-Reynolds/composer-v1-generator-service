package com.reynolds.composer.v1.generatorservice.persistence;

import com.reynolds.composer.v1.api.core.composition.composition.generated.CompositionVariation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CompositionVariationRepository extends ReactiveCrudRepository<CompositionVariation, String> {

    Flux<CompositionVariation> findByOriginalCompositionId(long originalCompositionId);

    Mono<Integer> countCompositionVariationByOriginalCompositionId(long originalCompositionId);

}

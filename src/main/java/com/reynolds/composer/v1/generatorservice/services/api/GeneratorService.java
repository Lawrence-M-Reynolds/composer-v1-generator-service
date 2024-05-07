package com.reynolds.composer.v1.generatorservice.services.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reynolds.composer.v1.api.core.composition.composition.Composition;
import com.reynolds.composer.v1.api.core.composition.composition.generated.CompositionVariation;

import java.util.List;

public interface GeneratorService {

    List<CompositionVariation> createVariations(Composition composition) throws JsonProcessingException;

    int getCountOfGeneratedForComposition(long compositionId);

    List<CompositionVariation> getVariationsForComposition(long compositionId) throws JsonProcessingException;
}

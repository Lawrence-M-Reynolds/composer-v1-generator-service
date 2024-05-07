package com.reynolds.composer.v1.generatorservice.persistence;

import com.reynolds.composer.v1.api.core.composition.composition.generated.CompositionVariation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CompositionVariationRepository extends PagingAndSortingRepository<CompositionVariation, String>,
        CrudRepository<CompositionVariation, String>{

    List<CompositionVariation> findByOriginalCompositionId(long originalCompositionId);

    int countCompositionVariationByOriginalCompositionId(long originalCompositionId);

}

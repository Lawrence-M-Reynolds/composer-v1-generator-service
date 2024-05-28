package com.reynolds.composer.v1.generatorservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reynolds.composer.v1.api.core.composition.composition.Composition;
import com.reynolds.composer.v1.api.core.composition.composition.generated.CompositionVariation;
import com.reynolds.composer.v1.generatorservice.persistence.CompositionVariationRepository;
import com.reynolds.composer.v1.generatorservice.services.api.GeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GeneratorServiceImpl implements GeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CompositionVariationRepository compositionVariationRepository;

    public GeneratorServiceImpl(CompositionVariationRepository compositionVariationRepository) {
        this.compositionVariationRepository = compositionVariationRepository;
    }

    @Override
    public Flux<CompositionVariation> createVariations(Composition composition) {
        List<CompositionVariation> compositionVariations = new ArrayList<>();

        compositionVariations.add(createVariation(composition, 1));
        compositionVariations.add(createVariation(composition, 2));
        compositionVariations.add(createVariation(composition, 3));

        logger.debug("Persisting variations: {}", compositionVariations);
        return compositionVariationRepository.saveAll(compositionVariations).log();
    }

    private CompositionVariation createVariation(Composition composition, int count) {
        CompositionVariation compositionVariation = new CompositionVariation();

        String originalCompositionName = Optional.ofNullable(composition.getName()).orElse("NO NAME");

        compositionVariation.setCompositionName(originalCompositionName + " | " + "compositionVariation_" + count);
        compositionVariation.setOriginalCompositionId(composition.getId());

        return compositionVariation;
    }

    @Override
    public Mono<Integer> getCountOfGeneratedForComposition(long compositionId) {
        return compositionVariationRepository.countCompositionVariationByOriginalCompositionId(compositionId);
    }

    @Override
    public Flux<CompositionVariation> getVariationsForComposition(long compositionId) throws JsonProcessingException {
        return compositionVariationRepository.findByOriginalCompositionId(compositionId);
    }

    /* TODO: Remove this OLD method for reference only. */
//    private List<Composition> createCompositions(Composition originalComposition) throws JsonProcessingException {
//        List<Composition> compositions = new ArrayList<>();
//
//        /* Creating mock variations. */
//        Composition compositionVariation1 = createComposition(originalComposition,
//                "compositionVariation1",
//                midiEvents -> {return Arrays.stream(midiEvents).map(i -> ++i).toArray(Integer[]::new);},
//                midiEvents -> {return Arrays.stream(midiEvents).map(i -> --i).toArray(Integer[]::new);}
//        );
//
//        logger.debug("compositionVariation1: {}", compositionVariation1);
//
//        Composition compositionVariation2 = createComposition(originalComposition,
//                "compositionVariation2",
//                midiEvents -> {return Arrays.stream(midiEvents).map(i -> i*2).toArray(Integer[]::new);},
//                midiEvents -> {return Arrays.stream(midiEvents).map(i -> i/2).toArray(Integer[]::new);}
//        );
//
//        logger.debug("compositionVariation2: {}", compositionVariation2);
//
//        Composition compositionVariation3 = createComposition(originalComposition,
//                "compositionVariation3",
//                midiEvents -> {return Arrays.stream(midiEvents).map(i -> i+5).toArray(Integer[]::new);},
//                midiEvents -> {return Arrays.stream(midiEvents).map(i -> i-5).toArray(Integer[]::new);}
//        );
//
//        logger.debug("compositionVariation3: {}", compositionVariation3);
//
//        return compositions;
//    }

    /* TODO: Remove this OLD method for reference only. */
//    private Composition createComposition(Composition composition,
//                                        String name,
//                                        Function<Integer[], Integer[]> track2Updater,
//                                        Function<Integer[], Integer[]>  track3Updater) throws JsonProcessingException {
//        Composition compositionVariation = new Composition(composition);
//        compositionVariation.setName(name);
//
//        List<Track> tracks = new ArrayList<>(compositionVariation.getTracks());
//
//        Track existingTrack = tracks.get(0);
//        logger.debug("existingTrack: {}", existingTrack);
//
//        Integer[] existingTrackMidiEvents = new ObjectMapper().readValue(existingTrack.getMidiNotes(), Integer[].class);
//
//        Track track2 = new Track(existingTrack);
//        track2.setMidiNotes(Arrays.toString(track2Updater.apply(existingTrackMidiEvents)));
//        logger.debug("track2: {}", track2);
//        tracks.add(track2);
//
//        Track track3 = new Track(existingTrack);
//        track3.setMidiNotes(Arrays.toString(track3Updater.apply(existingTrackMidiEvents)));
//        logger.debug("track3: {}", track3);
//        tracks.add(track3);
//
//        compositionVariation.setTracks(tracks);
//
//        return compositionVariation;
//    }
}

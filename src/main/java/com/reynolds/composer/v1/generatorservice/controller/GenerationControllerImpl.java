package com.reynolds.composer.v1.generatorservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reynolds.composer.v1.api.core.composition.composition.Composition;
import com.reynolds.composer.v1.api.core.composition.composition.Track;
import com.reynolds.composer.v1.api.core.generator.generator.GeneratorController;
import com.reynolds.composer.v1.generatorservice.services.CompositionServiceIntegration;
import com.reynolds.composer.v1.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

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
        Optional<Composition> compositionOptional = Optional.ofNullable(compositionServiceIntegration.getComposition(compositionId).getBody());

        if (compositionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Composition composition = compositionOptional.get();
        logger.debug("Composition: {}", composition);

        /* Creating mock variations. */
        Composition compositionVariation1 = createVariation(composition,
                "compositionVariation1",
                midiEvents -> {return Arrays.stream(midiEvents).map(i -> ++i).toArray(Integer[]::new);},
                midiEvents -> {return Arrays.stream(midiEvents).map(i -> --i).toArray(Integer[]::new);}
        );

        logger.debug("compositionVariation1: {}", compositionVariation1);

        Composition compositionVariation2 = createVariation(composition,
                "compositionVariation2",
                midiEvents -> {return Arrays.stream(midiEvents).map(i -> i*2).toArray(Integer[]::new);},
                midiEvents -> {return Arrays.stream(midiEvents).map(i -> i/2).toArray(Integer[]::new);}
        );

        logger.debug("compositionVariation2: {}", compositionVariation2);

        Composition compositionVariation3 = createVariation(composition,
                "compositionVariation3",
                midiEvents -> {return Arrays.stream(midiEvents).map(i -> i+5).toArray(Integer[]::new);},
                midiEvents -> {return Arrays.stream(midiEvents).map(i -> i-5).toArray(Integer[]::new);}
        );

        logger.debug("compositionVariation3: {}", compositionVariation3);

        /* Persisting mock variations. */

        // TODO - initiate generation here.
        return ResponseEntity.noContent().build();
    }

    private Composition createVariation(Composition composition,
                                        String name,
                                        Function<Integer[], Integer[]> track2Updater,
                                        Function<Integer[], Integer[]>  track3Updater) throws JsonProcessingException {
        Composition compositionVariation = new Composition(composition);
        compositionVariation.setName(name);

        List<Track> tracks = new ArrayList<>(compositionVariation.getTracks());

        Track existingTrack = tracks.get(0);
        logger.debug("existingTrack: {}", existingTrack);

        Integer[] existingTrackMidiEvents = new ObjectMapper().readValue(existingTrack.getMidiNotes(), Integer[].class);

        Track track2 = new Track(existingTrack);
        track2.setMidiNotes(Arrays.toString(track2Updater.apply(existingTrackMidiEvents)));
        logger.debug("track2: {}", track2);
        tracks.add(track2);

        Track track3 = new Track(existingTrack);
        track3.setMidiNotes(Arrays.toString(track3Updater.apply(existingTrackMidiEvents)));
        logger.debug("track3: {}", track3);
        tracks.add(track3);

        compositionVariation.setTracks(tracks);

        return compositionVariation;
    }

}

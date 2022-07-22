package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.model.Film;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.hamcrest.Matchers;
import org.hamcrest.MatcherAssert;

import java.text.ParseException;
import java.util.Collections;
import java.util.Map;

public class WorldArtObjectProducerTests {
//    @Test
//    void filmMapNullFilmData() {
//        Assertions.assertThrows(NullPointerException.class,
//                () -> {
//                    WorldArtObjectProducer.filmMap(null, null);
//                });
//    }
//
//    @Test
//    void filmMapEmptyFilmData() throws ParseException {
//        Map<String, String> filmData = Collections.emptyMap();
//        Film film = WorldArtObjectProducer.filmMap(filmData, null);
//        MatcherAssert.assertThat(film, Matchers.hasToString(Matchers.equalToIgnoringCase(
//                "Film(name=null, alternativeName=null, posterLink=null, countries=null," +
//                        " genres=null, worldPremier=null, duration=null, moneyEarnedWorldWide=null)"
//        )));
//    }
}
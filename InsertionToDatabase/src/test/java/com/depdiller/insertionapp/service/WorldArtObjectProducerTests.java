package com.depdiller.insertionapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WorldArtObjectProducerTests {
    @Test
    void regexPatternMatcherThrowsException() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
                RegexPatternMatcher.parseUsingPattern("Hello", "");
        });
    }
}
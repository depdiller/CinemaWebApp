package com.depdiller.insertionapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class RegexPatternMatcherTests {
    @Test
    void regexPatternMatcherThrowsExceptionNoGroupFound() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
                RegexPatternMatcher.parseUsingPattern("Hello", "");
        });
    }

    @Test
    void regexPatternMatcherEmptyGroup() {
        Assertions.assertEquals(Optional.empty(),
                RegexPatternMatcher.parseUsingPattern("world", "(^h.*)"));
    }

    @Test
    void regexPatternMatcherNullParseString() {
        Assertions.assertEquals(Optional.empty(),
                RegexPatternMatcher.parseUsingPattern(null, "(^h.*)"));
    }

    @Test
    void regexPatternMatcherNullPattern() {
        Assertions.assertEquals(Optional.empty(),
                RegexPatternMatcher.parseUsingPattern("Hello, world", null));
    }
}
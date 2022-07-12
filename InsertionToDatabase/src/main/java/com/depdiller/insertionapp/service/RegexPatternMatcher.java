package com.depdiller.insertionapp.service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexPatternMatcher {

    public static Optional<String> parseUsingPattern(String stringToParse, String regex) {
        if (stringToParse != null && regex != null) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(stringToParse);
            if (matcher.find())
                return Optional.ofNullable(matcher.group(1));
        }
        return Optional.empty();
    }
}

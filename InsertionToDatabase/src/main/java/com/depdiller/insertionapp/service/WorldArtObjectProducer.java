package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.model.Person;
import com.depdiller.insertionapp.model.TvShow;
import lombok.NonNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WorldArtObjectProducer {
    private static final String durationPattern = "(^[0-9]+)";
    private static final String worldPremierPattern = "([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})";
    private static final String moneyEarnedWorldWidePattern = "([0-9,.]+)";
    private static final String alternativeNamePattern = "(^\\S*)";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final ThreadLocal<DecimalFormat> FORMAT_THREAD_LOCAL =
            new ThreadLocal<>() {
                @Override
                protected DecimalFormat initialValue() {
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setGroupingSeparator(',');
                    symbols.setDecimalSeparator('.');
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.0#", symbols);
                    decimalFormat.setParseBigDecimal(true);
                    return decimalFormat;
                }
            };

    private enum WebsiteFilmTagNames {
        name("Русское название"), poster("Постер"),
        alternativeName("Названия"), countries("Производство"),
        duration("Хронометраж"), genres("Жанр"),
        worldPremier("Первый показ"), moneyEarnedWorldWide("Мировые сборы");

        private final String russianTag;

        WebsiteFilmTagNames(String russianTag) {
            this.russianTag = russianTag;
        }
    }

    public static Film filmMap(@NonNull Map<String, String> filmData) throws ParseException {
        String name = filmData.get(WebsiteFilmTagNames.name.russianTag);
        String poster = filmData.get(WebsiteFilmTagNames.poster.russianTag);

        List<String> countries = Optional.ofNullable(filmData.get(WebsiteFilmTagNames.countries.russianTag))
                .map(str -> List.of(str.split("(\\s+|,\\s+)")))
                .orElse(null);

        List<String> genres = Optional.ofNullable(filmData.get(WebsiteFilmTagNames.genres.russianTag))
                .map(str -> List.of(str.split("(\\s+|,\\s+)")))
                .orElse(null);

        String alternativeName = RegexPatternMatcher
                .parseUsingPattern(filmData.get(WebsiteFilmTagNames.alternativeName.russianTag), alternativeNamePattern)
                .orElse(null);

        LocalDate date = RegexPatternMatcher
                .parseUsingPattern(filmData.get(WebsiteFilmTagNames.worldPremier.russianTag), worldPremierPattern)
                .map(t -> LocalDate.parse(t, formatter))
                .orElse(null);

        Integer duration = RegexPatternMatcher
                .parseUsingPattern(filmData.get(WebsiteFilmTagNames.duration.russianTag), durationPattern)
                .map(Integer::parseInt)
                .orElse(null );

        BigDecimal money = RegexPatternMatcher
                .parseUsingPattern(filmData.get(WebsiteFilmTagNames.moneyEarnedWorldWide.russianTag), moneyEarnedWorldWidePattern)
                .map(str -> {
                    try {
                        return (BigDecimal) FORMAT_THREAD_LOCAL.get().parse(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .orElse(null);

        return Film.builder()
                .name(name)
                .alternativeName(alternativeName)
                .posterLink(poster)
                .countries(countries)
                .genres(genres)
                .worldPremier(date)
                .duration(duration)
                .moneyEarnedWorldWide(money)
                .build();
    }

    public Person personMap(Map<String, String> personData) {
        return null;
    }

    public TvShow tvShowMap(Map<String, String> tvShowData) {
        return null;
    }
}
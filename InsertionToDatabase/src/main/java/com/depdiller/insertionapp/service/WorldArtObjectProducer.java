package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.model.*;
import lombok.NonNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorldArtObjectProducer {
    private static final String durationPattern = "(^[0-9]+)";
    private static final String worldPremierPattern = "([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})";
    private static final String personBirthdatePattern = "([0-9]{2}\\.[0-9]{2}\\.[0-9]{4})";
    private static final String moneyEarnedWorldWidePattern = "([0-9,.]+)";
    private static final String alternativeNamePattern = "((.+?(?= \\/ ))|(.*$))";
    private static final DateTimeFormatter premierFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final DateTimeFormatter birthdateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String genderPattern = "^\\b(мужской|женский)\\b.*";
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

    private enum WebsitePersonTagNames {
        name("Имя"), birthdate("Дата рождения"), gender("Пол"),
        birthPlace("Место рождения");

        private final String russianTag;

        WebsitePersonTagNames(String russianTag) {
            this.russianTag = russianTag;
        }
    }

    public static Film filmMap(@NonNull Map<String, String> filmData, Map<String, String> links) {
        String name = filmData.get(WebsiteFilmTagNames.name.russianTag);
        String poster = filmData.get(WebsiteFilmTagNames.poster.russianTag);

        Set<Country> countries = Optional.ofNullable(filmData.get(WebsiteFilmTagNames.countries.russianTag))
                .map(str ->
                        Stream.of(str.split("(\\s+|,\\s+)"))
                                .map(Country::new)
                                .collect(Collectors.toSet())
                )
                .orElse(null);

        Set<Genre> genres = Optional.ofNullable(filmData.get(WebsiteFilmTagNames.genres.russianTag))
                .map(str -> Stream.of(str.split("(\\s+|,\\s+)"))
                        .map(Genre::new)
                        .collect(Collectors.toSet()))
                .orElse(null);

        String alternativeName = RegexPatternMatcher
                .parseUsingPattern(filmData.get(WebsiteFilmTagNames.alternativeName.russianTag), alternativeNamePattern)
                .orElse(null);

        LocalDate date = RegexPatternMatcher
                .parseUsingPattern(filmData.get(WebsiteFilmTagNames.worldPremier.russianTag), worldPremierPattern)
                .map(t -> LocalDate.parse(t, premierFormat))
                .orElse(null);

        Integer duration = RegexPatternMatcher
                .parseUsingPattern(filmData.get(WebsiteFilmTagNames.duration.russianTag), durationPattern)
                .map(Integer::parseInt)
                .orElse(null);

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

        Set<WebsiteLink> linksToFilm = links.keySet().stream()
                .filter(Objects::nonNull)
                .map(website ->
                        new String[]{website, links.get(website)}
                )
                .map(array -> {
                    Website website = new Website(array[0]);
                    WebsiteLink websiteLink = new WebsiteLink();
                    websiteLink.setWebsitename(website);
                    websiteLink.setLink(array[1]);
                    return websiteLink;
                })
                .collect(Collectors.toSet());

        return Film.builder()
                .name(name)
                .alternativeName(alternativeName)
                .posterLink(poster)
                .durationMinutes(duration)
                .genres(genres)
                .countries(countries)
                .moneyEarnedWorldWide(money)
                .worldPremier(date)
                .websiteLinks(linksToFilm)
                .personParticipationInFilms(new HashSet<>())
                .build();
    }

    public static Person personMap(Map<String, String> personData, Map<String, String> links) {
        String name = personData.get(WebsitePersonTagNames.name.russianTag);

        LocalDate birthdate = RegexPatternMatcher
                .parseUsingPattern(personData.get(WebsitePersonTagNames.birthdate.russianTag), personBirthdatePattern)
                .map(t -> LocalDate.parse(t, birthdateFormat))
                .orElse(null);

        Gender gender = RegexPatternMatcher
                .parseUsingPattern(personData.get(WebsitePersonTagNames.gender.russianTag), genderPattern)
                .map(Gender::getGender)
                .orElse(null);

        Place birthPlace = Optional.ofNullable(personData.get(WebsitePersonTagNames.birthPlace.russianTag))
                .map(placeString -> placeString.split("(\\s+|,\\s+)"))
                .map(array -> {
                    City city = new City(array[0]);
                    Country country = new Country(array[1]);
                    return new Place(city, country);
                })
                .orElse(null);

        Set<WebsiteLink> linksToPerson = links.keySet().stream()
                .filter(Objects::nonNull)
                .map(website ->
                        new String[]{website, links.get(website)}
                )
                .map(array -> {
                    Website website = new Website(array[0]);
                    WebsiteLink websiteLink = new WebsiteLink();
                    websiteLink.setWebsitename(website);
                    websiteLink.setLink(array[1]);
                    return websiteLink;
                })
                .collect(Collectors.toSet());

        return Person.builder()
                .name(name)
                .birthdate(birthdate)
                .gender(gender)
                .birthPlace(birthPlace)
                .websiteLinks(linksToPerson)
                .personParticipationInFilms(new HashSet<>())
                .build();
    }
}
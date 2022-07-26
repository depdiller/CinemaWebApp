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
    private static final String splitByCommaOrSpaceRegex = "(\\s+|,\\s+)";
    private static final String splitByCommaRegex = "(,\\s+)";
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

    public static Film filmMap(@NonNull Map<String, String> filmData, Set<WebsiteLink> links) {
        String name = filmData.get(WebsiteFilmTagNames.name.russianTag);
        String poster = filmData.get(WebsiteFilmTagNames.poster.russianTag);

        Set<Country> countries = Optional.ofNullable(filmData.get(WebsiteFilmTagNames.countries.russianTag))
                .map(str ->
                        Stream.of(str.split(splitByCommaRegex))
                                .map(Country::new)
                                .collect(Collectors.toSet())
                )
                .orElse(null);

        Set<Genre> genres = Optional.ofNullable(filmData.get(WebsiteFilmTagNames.genres.russianTag))
                .map(str -> Stream.of(str.split(splitByCommaRegex))
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


        Film film =  Film.builder()
                .name(name)
                .alternativeName(alternativeName)
                .posterLink(poster)
                .durationMinutes(duration)
                .genres(genres)
                .countries(countries)
                .moneyEarnedWorldWide(money)
                .worldPremier(date)
                .personParticipationInFilms(new ArrayList<>())
                .build();

        links.forEach(link -> {
            link.setFilm(film);
        });

        film.setWebsiteLinks(links);
        return film;
    }

    public static Person personMap(Map<String, String> personData, Set<WebsiteLink> links) {
        String name = personData.get(WebsitePersonTagNames.name.russianTag);

        LocalDate birthdate = RegexPatternMatcher
                .parseUsingPattern(personData.get(WebsitePersonTagNames.birthdate.russianTag), personBirthdatePattern)
                .map(t -> LocalDate.parse(t, birthdateFormat))
                .orElse(null);

        Gender gender = RegexPatternMatcher
                .parseUsingPattern(personData.get(WebsitePersonTagNames.gender.russianTag), genderPattern)
                .map(Gender::getGender)
                .orElse(null);

        String[] cityCountryNames = Optional.ofNullable(personData.get(WebsitePersonTagNames.birthPlace.russianTag))
                .map(placeString -> placeString.split(splitByCommaRegex))
                .map(array ->
                    new String[] {array[0], array[array.length - 1]}
                )
                .orElse(null);
        City city = Optional.ofNullable(cityCountryNames)
                .map(array -> new City(array[0])).orElse(null);
        Country country = Optional.ofNullable(cityCountryNames)
                .map(array -> new Country(array[array.length - 1])).orElse(null);

        Person person = Person.builder()
                .name(name)
                .birthdate(birthdate)
                .gender(gender)
                .birthCity(city)
                .personParticipationInFilms(new ArrayList<>())
                .birthCountry(country)
                .build();

        links.forEach(link -> link.setPerson(person));
        person.setWebsiteLinks(links);
        return person;
    }
}
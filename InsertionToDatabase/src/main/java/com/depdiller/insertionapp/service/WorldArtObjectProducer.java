package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.config.HibernateUtil;
import com.depdiller.insertionapp.model.*;
import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

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
    private static final ThreadLocal<Session> HIBERNATE_SESSION =
            new ThreadLocal<>() {
                @Override
                protected Session initialValue() {
                    return HibernateUtil.createSession();
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

    public static void filmMap(@NonNull Map<String, String> filmData, Map<String, String> links) {
        String name = filmData.get(WebsiteFilmTagNames.name.russianTag);
        String poster = filmData.get(WebsiteFilmTagNames.poster.russianTag);
        Session session = HIBERNATE_SESSION.get();
        Transaction tx = session.getTransaction();

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

        Film film = Film.builder()
                .name(name)
                .alternativeName(alternativeName)
                .posterLink(poster)
                .durationMinutes(duration)
                .genres(genres)
                .countries(countries)
                .moneyEarnedWorldWide(money)
                .worldPremier(date)
                .build();

        Set<WebsiteLink> linksNotAttachedToFilmYet = links.keySet().stream()
                .filter(Objects::nonNull)
                .map(website ->
                     new String[] { website, links.get(website) }
                )
                .map(array -> {
                    Website website = new Website(array[0]);
                    session.persist(website);
                    WebsiteLink websiteLink = new WebsiteLink();
                    websiteLink.setWebsitename(website);
                    websiteLink.setLink(array[1]);
                    session.persist(websiteLink);
                    return websiteLink;
                })
                .collect(Collectors.toSet());

        try {
            tx.begin();
            Optional.ofNullable(countries)
                    .ifPresent(countriesList -> countriesList.forEach(session::persist));
            Optional.ofNullable(genres)
                    .ifPresent(countriesList -> countriesList.forEach(session::persist));
            film.setWebsiteLinks(linksNotAttachedToFilmYet);
            session.persist(film);
            tx.commit();
        } catch (Exception ex) {
            try {
                if (tx.getStatus() == TransactionStatus.ACTIVE
                        || tx.getStatus() == TransactionStatus.MARKED_ROLLBACK)
                    tx.rollback();
            } catch (Exception rbEx) {
                System.err.println("Rollback of transaction failed, trace follows!");
                rbEx.printStackTrace(System.err);
            }
            throw new RuntimeException(ex);
        } finally {
            if (session.isOpen())
                session.close();
        }
    }

    public static void personMap(Map<String, String> personData, Map<String, String> links) {
        String name = personData.get(WebsitePersonTagNames.name.russianTag);

        LocalDate birthdate = RegexPatternMatcher
                .parseUsingPattern(personData.get(WebsitePersonTagNames.birthdate.russianTag), personBirthdatePattern)
                .map(t -> LocalDate.parse(t, birthdateFormat))
                .orElse(null);

        Gender gender = RegexPatternMatcher
                .parseUsingPattern(personData.get(WebsitePersonTagNames.gender.russianTag), genderPattern)
                .map(Gender::getGender)
                .orElse(null);

//        Place birthPlace = Optional.ofNullable(personData.get(WebsitePersonTagNames.birthPlace.russianTag))
//                .map(placeString -> placeString.split("(\\s+|,\\s+)"))
//                .map(place -> new Place(place[0], place[1]))
//                .orElse(null);

//        return Person.builder()
//                .name(name)
//                .birthdate(birthdate)
//                .gender(gender)
//                .birthPlace(birthPlace)
//                .linkOnOtherWebsites(links)
//                .build();
    }
}
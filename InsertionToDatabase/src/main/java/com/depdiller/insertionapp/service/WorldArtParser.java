package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.model.ParticipationValue;
import com.depdiller.insertionapp.model.Person;
import com.depdiller.insertionapp.model.PersonParticipationInFilm;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.NonNull;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorldArtParser implements Parser {
    private static final Set<String> russianFilmTags = Set.of("Названия", "Производство", "Хронометраж",
            "Жанр", "Первый показ", "Мировые сборы");
    private static final Set<String> russianPersonTags = Set.of("Дата рождения", "Пол", "Место рождения");

    private static final String filmTitleXpath = "/html/body/center/table[7]/tbody/tr/td/table/"
            + "tbody/tr/td[5]/table[2]/tbody/tr/td[4]/font";
    private static final String personPageTitleXpath = "/html/body/center/table[6]/tbody/tr/td/table[2]/" +
            "tbody/tr/td[4]/table[1]/tbody/tr/td[4]/table/tbody/tr/td[1]/font";

    private static final String posterXpath = "/html/body/center/table[7]/tbody/tr/td/"
            + "table/tbody/tr/td[5]/table[2]/tbody/tr/td[1]/div/table/tbody/tr/td/a";
    private static final String filmAttributesTable = "/html/body/center/table[7]/tbody/tr/td/table/"
            + "tbody/tr/td[5]/table[2]/tbody/tr/td[4]/table";

    private static final String personAttributesTable = "/html/body/center/table[6]/tbody/tr/td/table[2]/" +
            "tbody/tr/td[4]/table[1]/tbody/tr/td[4]/table/tbody/tr/td/table";
    private static final String metaDataKeywordsXpath = "/html/head/meta[2]";

    private static final WorldArtParser instance = new WorldArtParser();
    private WorldArtParser() {}
    public static WorldArtParser getInstance() {
        return instance;
    }

    @Override
    public Film filmParse(@NonNull HtmlPage filmPage) throws IllegalArgumentException {
        HtmlElement head = filmPage.getHead();
        HtmlMeta metaKeywords = filmPage
                .getHead()
                .getFirstByXPath(metaDataKeywordsXpath);
        String keywords = metaKeywords.getContentAttribute();
        String keywordsRegex = "^.*\\b(фильм)\\b.*";
        if (!keywords.matches(keywordsRegex))
            throw new IllegalArgumentException("Method can take only film page");

        HtmlFont titleElement = filmPage.getFirstByXPath(filmTitleXpath);
        String filmTitle = titleElement.asNormalizedText();

        HtmlAnchor posterUrlElement = filmPage.getFirstByXPath(posterXpath);
        String posterUrl = posterUrlElement.getHrefAttribute();

        List<HtmlTable> tables = filmPage.getByXPath(filmAttributesTable);
        Map<String, String> filmAttributes = tables.stream()
                .map(HtmlTable::asNormalizedText)
                .filter(Predicate.not(String::isEmpty))
                .map(str -> str.split("\t", 2))
                .peek(array -> {
                    array[0] = array[0].trim();
                    array[1] = array[1].trim();
                })
                .filter(array -> russianFilmTags.contains(array[0]))
                .collect(Collectors.toMap(array -> array[0], array -> array[1]));
        filmAttributes.put("Русское название", filmTitle);
        filmAttributes.put("Постер", posterUrl);

        Map<String, String> linksToFilm = LinksHandler.getLinks(filmPage);
        Film film = WorldArtObjectProducer.filmMap(filmAttributes, linksToFilm);
        HtmlAnchor castListAnchor = filmPage
                .getFirstByXPath("//a[contains(@href, 'cinema_full_cast.php')]");
        try {
            if (castListAnchor != null) {
                HtmlPage castPage = castListAnchor.click();
                Map<String, List<CompletableFuture<Person>>> rolesWithFuturePeople = FilmCastHandlerAsync
                        .parseFilmCastAsync(castPage);
                CompletableFuture[] result = rolesWithFuturePeople.keySet().stream()
                        .map(role -> {
                            var futurePeople = rolesWithFuturePeople.get(role);
                            CompletableFuture[] parsedPeople = futurePeople.stream()
                                    .peek(future -> future.thenAccept(person -> {
                                        ParticipationValue partType = new ParticipationValue(role);
                                        PersonParticipationInFilm participation = new PersonParticipationInFilm(
                                                person, film, partType
                                        );
                                        person.getPersonParticipationInFilms().add(participation);
                                        film.getPersonParticipationInFilms().add(participation);
                                    }))
                                    .toArray(CompletableFuture[]::new);
                            return parsedPeople;
                        })
                        .map(CompletableFuture::allOf)
                        .toArray(CompletableFuture[]::new);
                CompletableFuture.allOf(result).join();
                return film;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Person personParse(@NonNull HtmlPage page) {
        HtmlElement head = page.getHead();
        HtmlMeta metaKeywords = page
                .getHead()
                .getFirstByXPath(metaDataKeywordsXpath);
        String keywords = metaKeywords.getContentAttribute();
        String keywordsRegex = "^.*\\b(фильмография|список ролей|биография|год и место рождения)\\b.*";

        if (!keywords.matches(keywordsRegex))
            throw new IllegalArgumentException("Method can take only person page");

        HtmlFont personPageTitle = page.getFirstByXPath(personPageTitleXpath);
        String personName = personPageTitle.asNormalizedText();

        List<HtmlTable> tables = page.getByXPath(personAttributesTable);
        Map<String, String> personAttributes = tables.stream()
                .map(HtmlTable::asNormalizedText)
                .filter(Predicate.not(String::isEmpty))
                .map(str -> str.split("\t", 2))
                .peek(array -> {
                    array[0] = array[0].trim();
                    array[1] = array[1].trim();
                })
                .filter(array -> russianPersonTags.contains(array[0]))
                .collect(Collectors.toMap(array -> array[0], array -> array[1]));
        personAttributes.put("Имя", personName);

        Map<String, String> linksToPerson = LinksHandler.getLinks(page);
        return WorldArtObjectProducer.personMap(personAttributes, linksToPerson);
    }
}
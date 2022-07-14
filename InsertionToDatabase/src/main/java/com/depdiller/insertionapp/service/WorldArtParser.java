package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.model.Person;
import com.gargoylesoftware.htmlunit.html.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    @Override
    public Film filmParse(HtmlPage filmPage) throws IllegalArgumentException {
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

        Film film = null;
        try {
            film = WorldArtObjectProducer.filmMap(filmAttributes);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return film;
    }

    @Override
    public Person personParse(HtmlPage page) {
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

        Person person = WorldArtObjectProducer.personMap(personAttributes);
        return person;
    }
}
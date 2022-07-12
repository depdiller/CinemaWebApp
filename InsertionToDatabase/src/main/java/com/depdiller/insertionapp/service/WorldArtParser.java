package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.model.Person;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlFont;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WorldArtParser implements Parser {
    private static final Set<String> russianTags = Set.of("Русское название", "Постер", "Названия",
            "Производство", "Хронометраж", "Жанр", "Первый показ", "Мировые сборы");

    private static final String filmTitleXpath = "/html/body/center/table[7]/tbody/tr/td/table/"
            + "tbody/tr/td[5]/table[2]/tbody/tr/td[4]/font";
    private static final String posterXpath = "/html/body/center/table[7]/tbody/tr/td/"
            + "table/tbody/tr/td[5]/table[2]/tbody/tr/td[1]/div/table/tbody/tr/td/a";
    private static final String filmAttributesTable = "/html/body/center/table[7]/tbody/tr/td/table/"
            + "tbody/tr/td[5]/table[2]/tbody/tr/td[4]/table";

    @Override
    public Film filmParse(HtmlPage filmPage) throws IllegalArgumentException {
        String title = filmPage.getTitleText();
        String regexPattern = "^(.*(съемочная группа|актёрский состав)).*";

        if (title.matches(regexPattern)) {
            throw new IllegalArgumentException("Method takes only film pages");
        }

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
                .filter(array -> russianTags.contains(array[0]))
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
        return null;
    }
}

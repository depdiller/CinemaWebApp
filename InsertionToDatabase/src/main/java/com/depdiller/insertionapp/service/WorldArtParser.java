package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.model.Person;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlFont;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WorldArtParser implements Parser {
    private static final Set<String> russianTags = new HashSet<>() {{
        add("Названия");
        add("Производство");
        add("Хронометраж");
        add("Жанр");
        add("Первый показ");
        add("Мировые сборы");
    }};

    @Override
    public Film filmParse(HtmlPage filmPage) {
        String title = filmPage.getTitleText();
        String regexPattern = "^(.*(съемочная группа|актёрский состав)).*";

        if (title.matches(regexPattern)) {
            throw new IllegalArgumentException("Method takes only film pages");
        }

        HtmlFont titleElement = filmPage.getFirstByXPath("/html/body/center/table[7]/tbody/tr/td/table/"
                + "tbody/tr/td[5]/table[2]/tbody/tr/td[4]/font");
        String filmTitle = titleElement.asNormalizedText();

        HtmlAnchor posterUrlElement = filmPage.getFirstByXPath("/html/body/center/table[7]/tbody/tr/td/"
                + "table/tbody/tr/td[5]/table[2]/tbody/tr/td[1]/div/table/tbody/tr/td/a");
        String posterUrl = posterUrlElement.getHrefAttribute();

        List<HtmlTable> tables = filmPage.getByXPath("/html/body/center/table[7]/tbody/tr/td/table/"
                + "tbody/tr/td[5]/table[2]/tbody/tr/td[4]/table");

        Map<String, String> content = tables.stream()
                .map(HtmlTable::asNormalizedText)
                .filter(Predicate.not(String::isEmpty))
                .map(str -> str.split("\t", 2))
                .peek(array -> {
                    array[0] = array[0].trim();
                    array[1] = array[1].trim();
                })
                .filter(array -> russianTags.contains(array[0]))
                .collect(Collectors.toMap(array -> array[0], array -> array[1]));
        content.put("Русское название", filmTitle);
        content.put("Постер", posterUrl);
    }


    @Override
    public Person personParse(HtmlPage page) {
        return null;
    }
}

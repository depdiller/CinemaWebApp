package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.model.WebsiteFilmTagNames;
import com.gargoylesoftware.htmlunit.html.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WebPageParser {
    private static final Set<String> russianTags = new HashSet<>() {{
        add("Названия");
        add("Производство");
        add("Хронометраж");
        add("Жанр");
        add("Первый показ");
        add("Мировые сборы");
    }};

    public static Film filmParse(HtmlPage filmPage) throws IllegalArgumentException, ParseException {
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

        String alternativeName = content.get(WebsiteFilmTagNames.alternativeName.getRussianTag());
        List<String> countries = List.of(content.get(WebsiteFilmTagNames.countries.getRussianTag()).split("(\\s+|,\\s+)"));
        List<String> genres = List.of(content.get(WebsiteFilmTagNames.genres.getRussianTag()).split("(\\s+|,\\s+)"));

        Pattern pattern = Pattern.compile("([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})");
        Matcher matcher = pattern.matcher(content.get(WebsiteFilmTagNames.worldPremier.getRussianTag()));
        LocalDate date = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        if (matcher.find())
           date = LocalDate.parse(matcher.group(1), formatter);

        pattern = Pattern.compile("(^[0-9]+)");
        matcher = pattern.matcher(content.get(WebsiteFilmTagNames.duration.getRussianTag()));
        Integer duration = null;
        if (matcher.find())
            duration = Integer.parseInt(matcher.group(1));

        pattern = Pattern.compile("([0-9,.]+)");
        matcher = pattern.matcher(content.get(WebsiteFilmTagNames.moneyEarnedWorldWide.getRussianTag()));
        String moneyString = null;
        if (matcher.find())
            moneyString = matcher.group(1);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        String moneyPattern = "#,##0.0#";
        DecimalFormat decimalFormat = new DecimalFormat(moneyPattern, symbols);
        decimalFormat.setParseBigDecimal(true);

        BigDecimal money = (BigDecimal) decimalFormat.parse(moneyString);

        return Film.builder()
                .name(filmTitle)
                .alternativeName(alternativeName)
                .posterLink(posterUrl)
                .countries(countries)
                .genres(genres)
                .worldPremier(date)
                .duration(duration)
                .moneyEarnedWorldWide(money)
                .build();
    }
}
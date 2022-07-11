package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.model.Person;
import com.depdiller.insertionapp.model.TvShow;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class  WorldArtObjectProducer {
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
    private static final String durationPattern = "(^[0-9]+)";
    private static final String worldPremierPattern = "([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})";
    private static final String moneyEarnedWorldWidePattern = "([0-9,.]+)";
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

    public static Film filmMap(Map<String, String> filmData) throws ParseException {
        String name = filmData.get(WebsiteFilmTagNames.name.russianTag);
        String poster = filmData.get(WebsiteFilmTagNames.poster.russianTag);

        String alternativeName = filmData.get(WebsiteFilmTagNames.alternativeName.russianTag);
        List<String> countries = List.of(filmData.get(WebsiteFilmTagNames.countries.russianTag).split("(\\s+|,\\s+)"));
        List<String> genres = List.of(filmData.get(WebsiteFilmTagNames.genres.russianTag).split("(\\s+|,\\s+)"));

        Pattern pattern;
        Matcher matcher;

        String worldPremierValue = filmData.get(WebsiteFilmTagNames.worldPremier.russianTag);
        LocalDate date = null;
        if (worldPremierValue != null) {
            pattern = Pattern.compile(worldPremierPattern);
            matcher = pattern.matcher(worldPremierValue);
            if (matcher.find())
                date = LocalDate.parse(matcher.group(1), formatter);
        }

        String durationValue = filmData.get(WebsiteFilmTagNames.duration.russianTag);
        Integer duration = null;
        if (durationValue != null) {
            pattern = Pattern.compile(durationPattern);
            matcher = pattern.matcher(durationValue);
            if (matcher.find())
                duration = Integer.parseInt(matcher.group(1));
        }

        String moneyEarnedValue = filmData.get(WebsiteFilmTagNames.moneyEarnedWorldWide.russianTag);
        String moneyString = null;
        BigDecimal money = null;
        if (moneyEarnedValue != null) {
            pattern = Pattern.compile(moneyEarnedWorldWidePattern);
            matcher = pattern.matcher(moneyEarnedValue);
            if (matcher.find())
                moneyString = matcher.group(1);
            money = (BigDecimal) FORMAT_THREAD_LOCAL.get().parse(moneyString);
        }

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

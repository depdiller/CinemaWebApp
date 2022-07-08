package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.model.Person;
import com.depdiller.insertionapp.model.TvShow;
import com.depdiller.insertionapp.model.WebsiteFilmTagNames;

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

public class WorldArtMapper implements Mapper<String, String> {

    @Override
    public Film filmMap(Map<String, String> filmData) throws ParseException {
        String alternativeName = filmData.get(WebsiteFilmTagNames.alternativeName.getRussianTag());
        List<String> countries = List.of(filmData.get(WebsiteFilmTagNames.countries.getRussianTag()).split("(\\s+|,\\s+)"));
        List<String> genres = List.of(filmData.get(WebsiteFilmTagNames.genres.getRussianTag()).split("(\\s+|,\\s+)"));

        Pattern pattern = Pattern.compile("([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})");
        Matcher matcher = pattern.matcher(filmData.get(WebsiteFilmTagNames.worldPremier.getRussianTag()));
        LocalDate date = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        if (matcher.find())
            date = LocalDate.parse(matcher.group(1), formatter);

        pattern = Pattern.compile("(^[0-9]+)");
        matcher = pattern.matcher(filmData.get(WebsiteFilmTagNames.duration.getRussianTag()));
        Integer duration = null;
        if (matcher.find())
            duration = Integer.parseInt(matcher.group(1));

        pattern = Pattern.compile("([0-9,.]+)");
        matcher = pattern.matcher(filmData.get(WebsiteFilmTagNames.moneyEarnedWorldWide.getRussianTag()));
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
        return null;
    }

    @Override
    public Person personMap(Map<String, String> personData) {
        return null;
    }

    @Override
    public TvShow tvShowMap(Map<String, String> tvShowData) {
        return null;
    }
}

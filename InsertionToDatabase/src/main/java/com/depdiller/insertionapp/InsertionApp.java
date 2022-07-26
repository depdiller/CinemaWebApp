package com.depdiller.insertionapp;

import com.depdiller.insertionapp.database.DatabaseInserter;
import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.service.PageHandler;
import com.depdiller.insertionapp.service.Parser;
import com.depdiller.insertionapp.service.WorldArtParser;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.List;

public class InsertionApp {
    private static int numberOfFilmToBeginWith = 720;
    private static final int filmsOnOnePage = 50;
    public static void main(String... args) throws IOException {
        int from = 1;
        int until = 50;
        from += (numberOfFilmToBeginWith / filmsOnOnePage) * filmsOnOnePage;
        until += (numberOfFilmToBeginWith / filmsOnOnePage) * filmsOnOnePage;
        int index = numberOfFilmToBeginWith - from;

        long start = System.nanoTime();
        String customTopFilmsUrl = "http://www.world-art.ru/cinema/rating_top.php?limit_1=%d&&limit_2=%d";
        Parser parser = WorldArtParser.getInstance();
        DatabaseInserter databaseInserter = new DatabaseInserter();

        int count = numberOfFilmToBeginWith;
        for (; until < 7451; from += 50, until += 50) {
            String topFilmsInRange = String.format(customTopFilmsUrl, from, until);
            HtmlPage page = PageHandler.getPage(topFilmsInRange);
            List<HtmlAnchor> linksToFilms = page.getByXPath("//a[contains(@href, 'cinema.php?id=')]");
            for (int i = Math.max(index, 0); i < linksToFilms.size(); ++i) {
                long startFilm = System.nanoTime();
                HtmlPage filmPage = linksToFilms.get(i).click();
                Film film = parser.filmParse(filmPage);
                parser.parseFilmCastAndAttachToFilmObject(film, filmPage);
                databaseInserter.insertFilmInDatabase(film).join();
                System.out.println(count + ". film done in: " + ((System.nanoTime() - startFilm) / 1_000_000) + " milliseconds");
                count += 1;
            }
            index = 0;
        }
        System.out.println("All done in: " + ((System.nanoTime() - start) / 1_000_000) + " milliseconds");
    }
}
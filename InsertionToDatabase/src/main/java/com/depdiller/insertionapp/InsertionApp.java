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
    public static void main(String... args) throws IOException {
        long start = System.nanoTime();
//        String customTopFilmsUrl = "http://www.world-art.ru/cinema/rating_top.php?limit_1=%d&&limit_2=%d";
//        Parser parser = WorldArtParser.getInstance();
//        DatabaseInserter databaseInserter = new DatabaseInserter();
//        int i = 1;
//        int j = 50;
//        for (; j < 7451; i += 50, j += 50) {
//            String topFilmsInRange = String.format(customTopFilmsUrl, i, j);
//            HtmlPage page = PageHandler.getPage(topFilmsInRange);
//            List<HtmlAnchor> linksToFilms = page.getByXPath("//a[contains(@href, 'cinema.php?id=')]");
//            for (var anchor : linksToFilms) {
//                HtmlPage filmPage = anchor.click();
//                Film film = parser.filmParse(filmPage);
//                databaseInserter.insertFilmInDatabase(film);
//                System.out.println("Film done in: " + ((System.nanoTime() - start) / 1_000_000) + " milliseconds");
//            }
//            System.out.println(topFilmsInRange);
//        }

        Parser parser = WorldArtParser.getInstance();
        DatabaseInserter databaseInserter = new DatabaseInserter();
        for (int i = 4110; i < 4111; ++i) {
            String appendUrl = String.format("http://www.world-art.ru/cinema/cinema.php?id=%d", i);

            HtmlPage filmPage = PageHandler.getPage(appendUrl);
            Film film = parser.filmParse(filmPage);
            parser.parseFilmCastAndAttachToFilmObject(film, filmPage);

            databaseInserter.insertFilmInDatabase(film).join();
            System.out.println(i + ". Done in: " + ((System.nanoTime() - start) / 1_000_000) + " milliseconds");
        }
        System.out.println("Films added in: " + ((System.nanoTime() - start) / 1_000_000) + " milliseconds");
    }
}
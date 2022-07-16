package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.model.Film;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.List;

public class FullScraper {
    private static final String topFilmsUrl = "http://www.world-art.ru/cinema/rating_top.php";
    private static final Parser parser = WorldArtParser.getInstance();

    public void ScrapeTopFilmsList() throws IOException {
        HtmlPage filmsTop = PageHandler.getPage(topFilmsUrl);
        List<HtmlAnchor> cinemaRefsAnchors = filmsTop.getByXPath("//a[contains(@href, 'cinema.php')]");
        for (var hrefAnchor : cinemaRefsAnchors) {
            Film film = parser.filmParse(hrefAnchor.click());
            System.out.println(film);
        }
    }
}

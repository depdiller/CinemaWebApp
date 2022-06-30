package com.example.cinemawebapp.service;

import com.example.cinemawebapp.config.KinopoiskConfig;
import lombok.AllArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@AllArgsConstructor
@Component
public class FilmsTopScraperJsoup {
    private KinopoiskConfig kinopoiskConfig;

    public void ParseHtml() throws IOException {
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15" +
                " (KHTML, like Gecko) Version/15.5 Safari/605.1.15";
        Connection.Response response = Jsoup
                .connect(kinopoiskConfig.getUrlHome())
                .userAgent(userAgent)
                .method(Connection.Method.GET)
                .execute();

        System.out.println(response.cookies());

        Document pageWithTopFilms = Jsoup
                .connect(kinopoiskConfig.getUrlTop())
                .userAgent(userAgent)
                .cookies(response.cookies())
                .method(Connection.Method.GET)
                .get();

        Elements elements = pageWithTopFilms.select("form");

        String pageInString = pageWithTopFilms.outerHtml();

        BufferedWriter writer = new BufferedWriter(new FileWriter(
                "./src/main/resources/templates/kinopoiskJsoup.html")
        );
        writer.write(pageInString);
        writer.close();
    }
}

package com.example.cinemawebapp.service;

import com.example.cinemawebapp.config.FilmWebsiteConfig;
import com.example.cinemawebapp.config.HtmlunitWebClientConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Component
@AllArgsConstructor
public class Scraper {
    private FilmWebsiteConfig filmWebsiteConfig;
    private HtmlunitWebClientConfig htmlunitWebClientConfig;

    public void ParseHtml() throws IOException {
        WebClient webClient = htmlunitWebClientConfig.getWebClient();
        HtmlPage htmlPage = webClient
                .getPage(filmWebsiteConfig.getUrlTop());
        String htmlPageAsText = htmlPage.asXml();

        BufferedWriter writer = new BufferedWriter(new FileWriter(
                "./src/main/resources/templates/films.html")
        );

        writer.write(htmlPageAsText);
        writer.close();
    }
}
package com.example.cinemawebapp.service;

import com.example.cinemawebapp.config.KinopoiskConfig;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Component
@AllArgsConstructor
public class FilmsTopScraperHtmlunit {
    private KinopoiskConfig kinopoiskConfig;

    public void ParseHtml() throws IOException {
        final BrowserVersion browser = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.BEST_SUPPORTED)
                .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)" +
                        " AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.5 Safari/604.1.15")
                .build();
        WebClient webClient = new WebClient(browser);
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiesEnabled(true);
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        HtmlPage htmlPage = webClient.getPage(kinopoiskConfig.getUrlTop());
        String htmlPageAsText = htmlPage.asXml();

        BufferedWriter writer = new BufferedWriter(new FileWriter(
                "./src/main/resources/templates/films.html")
        );

        writer.write(htmlPageAsText);
        writer.close();
    }
}
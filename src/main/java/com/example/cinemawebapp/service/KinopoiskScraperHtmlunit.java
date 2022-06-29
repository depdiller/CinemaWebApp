package com.example.cinemawebapp.service;

import com.example.cinemawebapp.config.KinopoiskConfig;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Component
@AllArgsConstructor
public class KinopoiskScraperHtmlunit {
    private KinopoiskConfig kinopoiskConfig;

    public void ParseHtml() throws IOException {
        WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15" +
                " (KHTML, like Gecko) Version/15.5 Safari/605.1.15";
        webClient.addRequestHeader("User-Agent", userAgent);
        CookieManager cookieManager;
        cookieManager = webClient.getCookieManager();
        cookieManager.setCookiesEnabled(true);
        HtmlPage htmlPage = webClient.getPage(kinopoiskConfig.getUrl());
        webClient.waitForBackgroundJavaScriptStartingBefore(5000);
        String htmlPageAsText = htmlPage.asXml();

        BufferedWriter writer = new BufferedWriter(new FileWriter(
                "./src/main/resources/templates/kinopoisk.html")
        );
        writer.write(htmlPageAsText);
        writer.close();
    }
}
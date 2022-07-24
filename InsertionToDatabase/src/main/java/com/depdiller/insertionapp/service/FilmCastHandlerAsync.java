package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.config.ThreadSafeHtmlUnitConfig;
import com.depdiller.insertionapp.model.Person;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class FilmCastHandlerAsync {
    private static final String castTableDivsXpath = "/html/body/center/table[7]/tbody/tr/td/table/" +
            "tbody/tr/td[5]/table/tbody/tr/td/div";
    private static final String roleSubTablesXpath = "table[1]/tbody/tr/td/b";
    private static final Set<String> castRoles = Set.of("Режиссёры", "Сценаристы", "Актёры",
            "Продюсеры", "Операторы", "Композиторы");
    private static final Parser parser = WorldArtParser.getInstance();
    private static final String keywordsCastRegex = "^.*\\b(режиссер фильма|актеры фильма|" +
            "операторы фильма|продюсеры фильма|сценаристы фильма)\\b.*";
    private static final String metaDescription = ".//meta[contains(@name, 'description')]";
    private static final String peopleLinksByRolesXpath = ".//tr/td[2]/a[contains(@href, '../people.php')]";
    private static final String linkToWorldArt = "http://www.world-art.ru";
    private static final Executor executor =
            Executors.newFixedThreadPool(10,
                    (Runnable r) -> {
                        Thread thread = new Thread(r);
                        thread.setDaemon(true);
                        return thread;
                    }
            );

    public static Map<String, List<CompletableFuture<Person>>> parseFilmCastAsync(@NonNull HtmlPage filmCastPage) {
        HtmlMeta metaKeywords = filmCastPage
                .getHead()
                .getFirstByXPath(metaDescription);
        String keywords = metaKeywords.getContentAttribute();

        if (!keywords.matches(keywordsCastRegex))
            throw new IllegalArgumentException("Method can take only film cast pages");

        List<HtmlElement> tableDivs = filmCastPage
                .getByXPath(castTableDivsXpath);
        Map<String, HtmlElement> filmCastData = tableDivs.stream()
                .collect(Collectors.toMap(div -> {
                    HtmlBold tableNameElement = div.getFirstByXPath(roleSubTablesXpath);
                    return tableNameElement.asNormalizedText();
                }, div -> div));

        Map<String, List<CompletableFuture<Person>>> rolesWithFuturePeople = new HashMap<>();
        for (String role : castRoles) {
            HtmlElement roleElement = filmCastData.get(role);
            List<HtmlAnchor> roleTableAnchors = roleElement
                    .getByXPath(peopleLinksByRolesXpath);
            List<CompletableFuture<Person>> futurePeople = roleTableAnchors.stream()
                    .limit(5)
                    .map(anchor -> CompletableFuture.supplyAsync(() -> {
                        try {
                            String url = anchor.getHrefAttribute().replace("..", linkToWorldArt);
                            Thread.sleep((long)(Math.random() * 5000));
                            return ThreadSafeHtmlUnitConfig.getWebClient().<HtmlPage>getPage(url);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                            throw new RuntimeException();
                        }
                    }, executor))
                    .map(future -> future.thenApply(parser::personParse))
                    .collect(Collectors.toList());
            rolesWithFuturePeople.put(role, futurePeople);
        }
        return rolesWithFuturePeople;
    }
}

package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.config.ThreadSafeHtmlUnitConfig;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.NonNull;

import java.io.IOException;
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
    private static final String peopleXpath = "table[3]/tbody/tr/td[2]/a[contains(@href, '../people.php')]";
    private static final Set<String> castRoles = Set.of("Режиссёры", "Сценаристы", "Актёры",
            "Продюсеры", "Операторы", "Композиторы");
    private static final Parser parser = WorldArtParser.getInstance();
    private static final Executor executor =
            Executors.newFixedThreadPool(10,
                    (Runnable r) -> {
                        Thread thread = new Thread(r);
                        thread.setDaemon(true);
                        return thread;
                    }
            );

    public static CompletableFuture<Void> parseFilmCastAsync(@NonNull HtmlPage filmCastPage) {
        HtmlElement head = filmCastPage.getHead();
        HtmlMeta metaKeywords = filmCastPage
                .getHead()
                .getFirstByXPath(".//meta[contains(@name, 'description')]");
        String keywords = metaKeywords.getContentAttribute();
        String keywordsRegex = "^.*\\b(режиссер фильма|актеры фильма|" +
                "операторы фильма|продюсеры фильма|сценаристы фильма)\\b.*";

        if (!keywords.matches(keywordsRegex))
            throw new IllegalArgumentException("Method can take only film cast pages");

        List<HtmlElement> tableDivs = filmCastPage
                .getByXPath(castTableDivsXpath);
        Map<String, HtmlElement> filmCastData = tableDivs.stream()
                .collect(Collectors.toMap(div -> {
                    HtmlBold tableNameElement = div.getFirstByXPath(roleSubTablesXpath);
                    return tableNameElement.asNormalizedText();
                }, div -> div));

        CompletableFuture[] peopleFutures = castRoles.stream()
                .map(filmCastData::get)
                .map(element -> element.<HtmlAnchor>getByXPath(".//tr/td[2]/a[contains(@href, '../people.php')]"))
                .map(FilmCastHandlerAsync::startParseByRoleAsync)
                .toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf(peopleFutures);
    }

    private static CompletableFuture<Void> startParseByRoleAsync(List<HtmlAnchor> peopleRefsAnchors) {
        CompletableFuture[] futurePeople = peopleRefsAnchors.stream()
                .limit(5)
                .map(anchor -> CompletableFuture.supplyAsync(() -> {
                    try {
                        String url = anchor.getHrefAttribute().replace("..", "http://www.world-art.ru");
                        HtmlPage page = ThreadSafeHtmlUnitConfig.getWebClient().getPage(url);
//                        HtmlPage page = anchor.click();
                        return page;
                    } catch (IOException e) {
                        throw new RuntimeException();
                    }
                }, executor))
                .map(future -> future.thenApply(parser::personParse))
                .toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf(futurePeople);
    }
}

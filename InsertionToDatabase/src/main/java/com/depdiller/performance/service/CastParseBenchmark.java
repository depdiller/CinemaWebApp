package com.depdiller.performance.service;

import com.depdiller.insertionapp.config.ThreadSafeHtmlUnitConfig;
import com.depdiller.insertionapp.service.PageHandler;
import com.depdiller.insertionapp.service.Parser;
import com.depdiller.insertionapp.service.WorldArtParser;
import com.gargoylesoftware.htmlunit.html.*;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(value = Scope.Benchmark)
public class CastParseBenchmark {
    private static final String castUrl = "http://www.world-art.ru/cinema/cinema_full_cast.php?id=4110";
    private static final String castDivs = "/html/body/center/table[7]/tbody/tr/td/table/tbody/tr/td[5]/table/tbody/tr/td/div";
    private static final String subTablesXpath = "table[1]/tbody/tr/td/b";
    private static final Set<String> castRoles = Set.of("Режиссёры", "Сценаристы", "Актёры", "Продюсеры", "Операторы", "Композиторы");
    private final Executor executor =
            Executors.newFixedThreadPool(1,
                    (Runnable r) -> {
                        Thread thread = new Thread(r);
                        thread.setDaemon(true);
                        return thread;
                    }
            );

    private class RoleConverter {
        static final String peopleXpath = "table[3]/tbody/tr/td[2]/a[contains(@href, '../people.php')]";
        private final Parser parser;
        private final Map<String, HtmlElement> castData;

        public RoleConverter(Parser parser, Map<String, HtmlElement> castData) {
            this.parser = parser;
            this.castData = castData;
        }

        public CompletableFuture<Void> parse(String role, int i) {
            HtmlElement roleData = castData.get(role);
            System.out.println("\n\n" + roleData.asXml());
            List<HtmlAnchor> personRefsAnchors = roleData
                    .getByXPath("//a[contains(@href, '../people.php')]");

            CompletableFuture[] futurePeople = personRefsAnchors.stream()
                    .limit(1)
                    .map(anchor -> CompletableFuture.supplyAsync(() -> {
                        try {
                            String url = anchor.getHrefAttribute().replace("..", "http://www.world-art.ru");
                            HtmlPage page = ThreadSafeHtmlUnitConfig.getWebClient().getPage(url);
//                            HtmlPage page = anchor.click();
                            return page;
                        } catch (IOException e) {
                            throw new RuntimeException();
                        }
                    }, executor))
                    .map(future -> future.thenApply(parser::personParse))
//                    .map(future -> future.thenAccept(System.out::println))
                    .toArray(CompletableFuture[]::new);
            return CompletableFuture.allOf(futurePeople);
        }
    }

    @Benchmark
    public void sequentialParse() throws Exception {
        long start = System.nanoTime();

        HtmlPage page = PageHandler.getPage(castUrl);
        List<HtmlElement> divs = page
                .getByXPath(castDivs);

        Map<String, HtmlElement> castData = new HashMap<>();
        for (var div : divs) {
            HtmlBold tableNameElement = div.getFirstByXPath(subTablesXpath);
            String role = tableNameElement.asNormalizedText();
            castData.put(role, div);
        }

        Parser worldArtParser = WorldArtParser.getInstance();
        RoleConverter roleConverter = new RoleConverter(worldArtParser, castData);
        CompletableFuture[] resultFutures = new CompletableFuture[castRoles.size()];

        int i = 0;
        for (var role : castRoles) {
            resultFutures[i] = roleConverter.parse(role, i);
            ++i;
        }
        CompletableFuture.allOf(resultFutures).join();

        System.out.println(((System.nanoTime() - start) / 1_000_000 ));
    }
}
package com.depdiller.insertionapp;

import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.service.FullScraper;
import com.depdiller.insertionapp.service.PageHandler;
import com.depdiller.insertionapp.service.Parser;
import com.depdiller.insertionapp.service.WorldArtParser;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InsertionApp {
    public static void main(String... args) {
        String castDivs = "/html/body/center/table[7]/tbody/tr/td/table/tbody/tr/td[5]/table/tbody/tr/td/div";
        String castUrl = "http://www.world-art.ru/cinema/cinema_full_cast.php?id=5397";
        String subTablesXpath = "table[1]/tbody/tr/td/b";
        Set<String> castRoles = Set.of("Режиссёры", "Сценаристы", "Актёры", "Продюсеры", "Операторы", "Композиторы");
        try {
            List<HtmlElement> divs = PageHandler.getPage(castUrl)
                    .getByXPath(castDivs);
            Map<String, HtmlElement> castData = divs.stream()
                    .collect(Collectors.toMap(div -> {
                        HtmlBold tableNameElement = div.getFirstByXPath(subTablesXpath);
                        return tableNameElement.asNormalizedText();
                    }, div -> div));

            ConcurrentHashMap<String, List<HtmlPage>> castPages = new ConcurrentHashMap<>();
            castRoles.stream()
                    .map(role -> {
                        HtmlElement roleData = castData.get(role);
                        List<HtmlAnchor> personRefsAnchors = roleData.getByXPath("table[3]/tbody/tr/td[2]/a[contains(@href, '../people.php')]");
                        List<HtmlPage> htmlPageList = new ArrayList<>();
                        for (var anchor : personRefsAnchors) {
                            try {
                                htmlPageList.add(anchor.click());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        castPages.put(role, htmlPageList);
                        return null;
//                        List<HtmlPage> htmlPageList = personRefsAnchors.stream()
//                                .map(htmlAnchor -> {
//                                    try {
//                                        return htmlAnchor.<HtmlPage>click();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    return null;
//                                }).toList();
                    }).forEach(t -> {});



//            Map<String, List<HtmlPage>> castPages = castRoles.stream()
//                    .collect(Collectors.toMap(role -> role, role -> {
//                        HtmlElement roleData = castData.get(role);
//                        List<HtmlAnchor> personRefsAnchors = roleData.getByXPath("table[3]/tbody/tr/td[2]/a[contains(@href, '../people.php')]");
//                        return personRefsAnchors.stream()
//                                .map(htmlAnchor -> {
//                                    try {
//                                        return htmlAnchor.<HtmlPage>click();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    return null;
//                                })
//                                .collect(Collectors.toList());
//                    }));

            System.out.println("Actors:");
            Parser parser = new WorldArtParser();
            for (var person : castPages.get("Актёры")) {
                System.out.println(parser.personParse(person));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        FullScraper scraper = new FullScraper();
//        try {
//            scraper.ScrapeTopFilmsList();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}

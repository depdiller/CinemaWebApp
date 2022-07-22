package com.depdiller.insertionapp.service;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.NonNull;

import java.util.*;

public class LinksHandler {
    private static final Set<String> websitesToSearchLinksFrom = Set.of("kinopoisk.ru",
            "imdb.com", "rottentomatoes.com", "metacritic.com");

    public static Map<String, String> getLinks(@NonNull HtmlPage page) {
        Map<String, String> resultLinks = new HashMap<>();
        websitesToSearchLinksFrom.stream()
                .map(website -> {
                    String xpath = String.format("//a[contains(@href, '%s')]", website);
                    return new String[] {website, xpath};
                })
                .peek(websiteNameAndXpath -> {
                    String filmLink = Optional.ofNullable(page
                                    .<HtmlAnchor>getFirstByXPath(websiteNameAndXpath[1]))
                            .map(HtmlAnchor::getHrefAttribute)
                            .orElse(null);
                    websiteNameAndXpath[1] = filmLink;
                })
                .filter(array -> Objects.nonNull(array[1]))
                .forEach(nameAndLink -> resultLinks.put(nameAndLink[0], nameAndLink[1]));
        return resultLinks;
    }
}
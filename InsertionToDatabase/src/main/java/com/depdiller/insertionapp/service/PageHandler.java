package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.config.HtmlunitWebClientConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public class PageHandler {
    public static HtmlPage getPage(String url) throws IOException {
        WebClient client = HtmlunitWebClientConfig.getWebClient();
        return client.getPage(url);
    }
}

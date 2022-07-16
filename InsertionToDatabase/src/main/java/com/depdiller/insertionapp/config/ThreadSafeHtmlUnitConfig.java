package com.depdiller.insertionapp.config;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;

public class ThreadSafeHtmlUnitConfig {
    private static final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
            "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.5 Safari/604.1.15";
    private static final BrowserVersion browserVersion = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.CHROME)
            .setUserAgent(userAgent)
            .build();

    public static WebClient getWebClient() {
        WebClient webClient = new WebClient(browserVersion);
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiesEnabled(true);
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setAppletEnabled(false);
        webClient.getOptions().setDownloadImages(false);
        webClient.getOptions().setDoNotTrackEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        return webClient;
    }
}
package com.depdiller.insertionapp.config;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;

public class HtmlunitWebClientConfig {
    private static class ConfigHolder {
        private static final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.5 Safari/604.1.15";
        private static final BrowserVersion browserVersion = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.CHROME)
                .setUserAgent(userAgent)
                .build();
        private static final CookieManager cookieManager = new CookieManager();
        private static final WebClient webClientInstance = new WebClient(browserVersion);
        public static WebClient getWebClientInstance() {
            cookieManager.setCookiesEnabled(false);
            webClientInstance.setCookieManager(cookieManager);
            webClientInstance.getOptions().setUseInsecureSSL(true);
            webClientInstance.getOptions().setCssEnabled(false);
            webClientInstance.getOptions().setAppletEnabled(false);
            webClientInstance.getOptions().setDownloadImages(false);
            webClientInstance.getOptions().setDoNotTrackEnabled(true);
            webClientInstance.getOptions().setJavaScriptEnabled(false);
            webClientInstance.getOptions().setThrowExceptionOnScriptError(false);
            webClientInstance.getOptions().setThrowExceptionOnFailingStatusCode(true);
            return webClientInstance;
        }
    }

    private HtmlunitWebClientConfig() {
    }

    public static WebClient getWebClient() {
        return ConfigHolder.getWebClientInstance();
    }
}
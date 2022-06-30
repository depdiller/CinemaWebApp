package com.example.cinemawebapp.config;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "web-client-prop")
@ConstructorBinding
public class HtmlunitWebClientConfig {
    private final String userAgent;

    public HtmlunitWebClientConfig(String userAgent) {
        this.userAgent = userAgent;
    }

    public WebClient getWebClient() {
        final BrowserVersion browserVersion = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.BEST_SUPPORTED)
                .setUserAgent(userAgent)
                .build();

        WebClient webClient = new WebClient(browserVersion);
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiesEnabled(true);
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        return webClient;
    }
}

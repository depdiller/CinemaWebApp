package com.example.cinemawebapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "film-website")
@Data
public class FilmWebsiteConfig {
    private String urlHome;
    private String urlTop;
}

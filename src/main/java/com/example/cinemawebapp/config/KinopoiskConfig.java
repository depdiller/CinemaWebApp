package com.example.cinemawebapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kinopoisk")
@Data
public class KinopoiskConfig{
    private String url;
}

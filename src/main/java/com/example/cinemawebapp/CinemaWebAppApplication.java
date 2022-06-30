package com.example.cinemawebapp;

import com.example.cinemawebapp.config.FilmWebsiteConfig;
import com.example.cinemawebapp.config.HtmlunitWebClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FilmWebsiteConfig.class,
        HtmlunitWebClientConfig.class})
public class CinemaWebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(CinemaWebAppApplication.class, args);
    }
}
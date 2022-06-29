package com.example.cinemawebapp;

import com.example.cinemawebapp.service.KinopoiskScraperHtmlunit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(KinopoiskScraperHtmlunit.class)
public class CinemaWebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(CinemaWebAppApplication.class, args);
    }
}
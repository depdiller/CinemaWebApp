package com.example.cinemawebapp;

import com.example.cinemawebapp.config.KinopoiskConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(KinopoiskConfig.class)
public class CinemaWebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(CinemaWebAppApplication.class, args);
    }
}
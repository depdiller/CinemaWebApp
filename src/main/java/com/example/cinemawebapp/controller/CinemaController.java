package com.example.cinemawebapp.controller;

import com.example.cinemawebapp.service.KinopoiskScraperHtmlunit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@AllArgsConstructor
public class CinemaController {
    private final KinopoiskScraperHtmlunit kinopoiskScraper;

    @GetMapping("/home")
    public String getHomePage() {
        try {
            kinopoiskScraper.ParseHtml();
            return "kinopoisk";
        } catch (IOException e) {
            return "error";
        }
    }
}

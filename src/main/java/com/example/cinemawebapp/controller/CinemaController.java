package com.example.cinemawebapp.controller;

import com.example.cinemawebapp.service.Scraper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@AllArgsConstructor
public class CinemaController {
    private final Scraper kinopoiskScraperHtmlunit;

    @GetMapping("/home")
    public String getHomePage() {
        try {
            kinopoiskScraperHtmlunit.ParseHtml();
            return "films";
        } catch (IOException e) {
            return "error";
        }
    }
}

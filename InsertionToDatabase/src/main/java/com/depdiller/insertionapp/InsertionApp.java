package com.depdiller.insertionapp;

import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.service.PageHandler;
import com.depdiller.insertionapp.service.WebPageParser;

import java.io.IOException;
import java.text.ParseException;

public class InsertionApp {
    public static void main(String... args) {
        String filmUrl = "http://www.world-art.ru/cinema/cinema.php?id=30395";
        try {
            Film film = WebPageParser.filmParse(
                    PageHandler.getPage(filmUrl)
            );
            System.out.println(film);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}

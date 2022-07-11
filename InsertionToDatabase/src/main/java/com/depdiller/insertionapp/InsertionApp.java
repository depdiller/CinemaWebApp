package com.depdiller.insertionapp;

import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.service.PageHandler;
import com.depdiller.insertionapp.service.Parser;
import com.depdiller.insertionapp.service.WorldArtParser;

import java.io.IOException;

public class InsertionApp {
    public static void main(String... args) {
        String filmUrl = "http://www.world-art.ru/cinema/cinema.php?id=30395";
        Parser webParser = new WorldArtParser();
        try {
            Film film = webParser.filmParse(
                    PageHandler.getPage(filmUrl)
            );
            System.out.println(film);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

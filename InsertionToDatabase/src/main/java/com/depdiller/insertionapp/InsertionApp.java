package com.depdiller.insertionapp;

import com.depdiller.insertionapp.service.PageHandler;
import com.depdiller.insertionapp.service.Parser;
import com.depdiller.insertionapp.service.WorldArtParser;

import java.io.IOException;

public class InsertionApp {
    public static void main(String... args) throws IOException {
        String filmUrl;
        Parser parser = WorldArtParser.getInstance();
        for (int i = 4110; i < 4120; ++i) {
            filmUrl = String.format("http://www.world-art.ru/cinema/cinema.php?id=%d", i);
            parser.filmParse(PageHandler.getPage(filmUrl));
        }
    }
}
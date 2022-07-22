package com.depdiller.insertionapp;

import com.depdiller.insertionapp.service.PageHandler;
import com.depdiller.insertionapp.service.Parser;
import com.depdiller.insertionapp.service.WorldArtParser;

import java.io.IOException;
import java.util.stream.IntStream;

public class InsertionApp {
    public static void main(String... args) throws IOException {
        Parser parser = WorldArtParser.getInstance();
        IntStream.range(4110, 4120).parallel()
                .forEach(i -> {
                    String filmUrl = String.format("http://www.world-art.ru/cinema/cinema.php?id=%d", i);
                    try {
                        parser.filmParse(PageHandler.getPage(filmUrl));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
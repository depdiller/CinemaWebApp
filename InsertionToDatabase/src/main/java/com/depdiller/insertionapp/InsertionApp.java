package com.depdiller.insertionapp;

import com.depdiller.insertionapp.database.DatabaseInserter;
import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.service.PageHandler;
import com.depdiller.insertionapp.service.Parser;
import com.depdiller.insertionapp.service.WorldArtParser;

import java.io.IOException;

public class InsertionApp {
    public static void main(String... args) throws IOException {
        long start = System.nanoTime();

        String url = "http://www.world-art.ru/cinema/cinema.php?id=4110";
        Parser parser = WorldArtParser.getInstance();
        Film film = parser.filmParse(PageHandler.getPage(url));
        DatabaseInserter databaseInserter = new DatabaseInserter();
        databaseInserter.insertFilmInDatabase(film).join();

        System.out.println("Done in: " + ((System.nanoTime() - start) / 1_000_000) + " milliseconds");
    }
}
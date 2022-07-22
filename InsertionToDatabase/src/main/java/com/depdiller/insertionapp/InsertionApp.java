package com.depdiller.insertionapp;

import com.depdiller.insertionapp.config.HibernateUtil;
import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.service.PageHandler;
import com.depdiller.insertionapp.service.Parser;
import com.depdiller.insertionapp.service.WorldArtParser;
import jakarta.persistence.EntityManager;

import java.io.IOException;
import java.util.stream.IntStream;

public class InsertionApp {
    public static void main(String... args) throws IOException {
        long start = System.nanoTime();
        Parser parser = WorldArtParser.getInstance();
        String url = "http://www.world-art.ru/cinema/cinema.php?id=4110";
//        IntStream.range(4110, 4120).parallel()
//                .forEach(i -> {
//                    String filmUrl = String.format("http://www.world-art.ru/cinema/cinema.php?id=%d", i);
//                    try {
//                        parser.filmParse(PageHandler.getPage(filmUrl));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
        Film film = parser.filmParse(PageHandler.getPage(url));
        System.out.println("Done in: " + ((System.nanoTime() - start) / 1_000_000) + " milliseconds");
//        EntityManager em = HibernateUtil.creatEntityManager();
//        em.persist(film);
//        em.close();
    }
}
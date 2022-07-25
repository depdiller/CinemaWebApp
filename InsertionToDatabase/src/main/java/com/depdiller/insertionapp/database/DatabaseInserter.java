package com.depdiller.insertionapp.database;

import com.depdiller.insertionapp.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor
public class DatabaseInserter {
    public CompletableFuture<Void> insertFilmInDatabase(@NonNull Film film) {
        EntityManager entityManager = HibernateUtil.creatEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        return CompletableFuture.runAsync(() -> {
            transaction.begin();

            Set<Genre> genres = film.getGenres();
            if (genres != null) {
                genres.forEach(genre -> {
                    Genre searchedGenre = entityManager.find(Genre.class, genre.getGenre());
                    if (searchedGenre == null)
                        entityManager.persist(genre);
                });
            }

            Set<Country> countries = film.getCountries();
            if (countries != null) {
                countries.forEach(country -> {
                    Country searchedCountry = entityManager.find(Country.class, country.getCountryName());
                    if (searchedCountry == null)
                        entityManager.persist(country);
                });
            }

            Set<WebsiteLink> filmWebsiteLinks = film.getWebsiteLinks();
            if (filmWebsiteLinks != null) {
                filmWebsiteLinks.forEach(websiteLink -> {
                    Website website = websiteLink.getWebsite();
                    Website searchedWebsite = entityManager.find(Website.class, website.getWebsiteName());
                    if (searchedWebsite == null)
                        entityManager.persist(website);

                    WebsiteLink searchedLink = entityManager.find(WebsiteLink.class, websiteLink.getLink());
                    if (searchedLink == null)
                        entityManager.persist(websiteLink);
                });
            }

            Set<PersonParticipationInFilm> participants = film.getPersonParticipationInFilms();
            participants.forEach(participant -> {
                Person person = participant.getPerson();

                City city = person.getBirthCity();
                if (city != null) {
                    City searchedCity = entityManager.find(City.class, city.getCityName());
                    if (searchedCity == null)
                        entityManager.persist(city);
                }
                Country country = person.getBirthCountry();
                if (country != null) {
                    Country searchedCountry = entityManager.find(Country.class, country.getCountryName());
                    if (searchedCountry == null)
                        entityManager.persist(country);
                }

                ParticipationValue partType = participant.getPartType();
                ParticipationValue searchedPartType = entityManager.find(ParticipationValue.class, partType.getPartType());
                if (searchedPartType == null) {
                    entityManager.persist(partType);
                }

                Set<WebsiteLink> personWebsiteLinks = person.getWebsiteLinks();
                personWebsiteLinks.forEach(websiteLink -> {
                    Website website = websiteLink.getWebsite();
                    Website searchedWebsite = entityManager.find(Website.class, website.getWebsiteName());
                    if (searchedWebsite == null)
                        entityManager.persist(website);

                    WebsiteLink searchedLink = entityManager.find(WebsiteLink.class, websiteLink.getLink());
                    if (searchedLink == null) {
                        entityManager.persist(websiteLink);
                        entityManager.persist(person);
                    }
                    searchedLink.getPeople();
                });
            });
            entityManager.persist(film);

            transaction.commit();
            entityManager.close();
        });
    }
}

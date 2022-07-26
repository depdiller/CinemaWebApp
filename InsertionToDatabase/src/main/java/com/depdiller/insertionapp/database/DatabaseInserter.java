package com.depdiller.insertionapp.database;

import com.depdiller.insertionapp.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.*;
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

            List<PersonParticipationInFilm> participants = film.getPersonParticipationInFilms();
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
                if (!personWebsiteLinks.isEmpty()) {
                    personWebsiteLinks.forEach(websiteLink -> {
                        WebsiteLink searchedLink = entityManager.find(WebsiteLink.class, websiteLink.getLink());
                        if (searchedLink == null) {
                            entityManager.persist(person);
                            entityManager.persist(websiteLink);
                        } else {
                            Person foundPerson = searchedLink.getPerson();
                            participant.setPerson(foundPerson);
                        }
                    });
                }
                else {
                    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                    CriteriaQuery<Person> cr = cb.createQuery(Person.class);
                    Root<Person> root = cr.from(Person.class);
                    cr.select(root).where(cb.equal(root.get("name"), person.getName()));
                    Optional<Person> foundPerson = entityManager.createQuery(cr)
                            .getResultStream()
                            .findFirst();

                    if (foundPerson.isEmpty())
                        entityManager.persist(person);
                    else
                        participant.setPerson(foundPerson.get());
                }
            });

            Set<WebsiteLink> filmWebsiteLinks = film.getWebsiteLinks();
            filmWebsiteLinks.forEach(websiteLink -> {
                WebsiteLink searchedLink = entityManager.find(WebsiteLink.class, websiteLink.getLink());
                if (searchedLink == null)
                    entityManager.persist(websiteLink);
            });
            entityManager.persist(film);

            transaction.commit();
            entityManager.close();
        });
    }
}
package com.depdiller.insertionapp.database;

import com.depdiller.insertionapp.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.Session;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor
public class DatabaseInserter {
    private EntityTransaction transaction;
    private EntityManager entityManager;
    private static final ThreadLocal<EntityManager> HIBERNATE_SESSION =
            new ThreadLocal<>() {
                @Override
                protected EntityManager initialValue() {
                    return HibernateUtil.creatEntityManager();
                }
            };

    {
        entityManager = HIBERNATE_SESSION.get();
        transaction = entityManager.getTransaction();
    }

    public CompletableFuture<Void> insertFilmInDatabase(@NonNull Film film) {
        return CompletableFuture.runAsync(() -> {
            transaction.begin();
            entityManager.persist(film);
            transaction.commit();
            entityManager.close();
//            Set<PersonParticipationInFilm> participations = film.getPersonParticipationInFilms();
//            film.setPersonParticipationInFilms(null);
//            entityManager.persist(film);
//            participations.forEach(participation -> {
//                entityManager.merge(participation.getParttype());
//            });
//
//            participations.forEach(participation -> {
//                Person person = participation.getPersonid();
//                entityManager.persist(person);
//                Place place = person.getBirthPlace();
//                if (place != null) {
//                    entityManager.merge(place.getCityname());
//                    entityManager.merge(place.getCountryname());
//                    entityManager.merge(place);
//                }
//
//                Set<WebsiteLink> websiteLinks = person.getWebsiteLinks();
//                if (websiteLinks != null) {
//                    websiteLinks.forEach(link -> {
//                        entityManager.merge(link.getWebsitename());
//                        entityManager.persist(link);
//                    });
//                }
//                entityManager.persist(participation);
//            });
//
//            Set<Country> countries = film.getCountries();
//            if (countries != null) {
//                countries.forEach(country -> {
//                    entityManager.persist(country);
//                });
//            }
//
//            Set<WebsiteLink> websiteLinks = film.getWebsiteLinks();
//            if (websiteLinks != null) {
//                websiteLinks.forEach(link -> {
//                    entityManager.persist(link.getWebsitename());
//                    entityManager.persist(link);
//                });
//            }
//
//            Set<Genre> genres = film.getGenres();
//            if (genres != null) {
//                genres.forEach(genre -> {
//                    entityManager.persist(genre);
//                });
//            }
//            film.setPersonParticipationInFilms(participations);
//            entityManager.unwrap(Session.class).update(film);
//            entityManager.persist(film);
        });
    }
}

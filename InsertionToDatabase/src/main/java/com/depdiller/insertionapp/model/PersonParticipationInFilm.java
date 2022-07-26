package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PersonParticipationInFilm {
    public PersonParticipationInFilm(Person person, Film film, ParticipationValue partType) {
        this.partType = partType;
        this.person = person;
        this.film = film;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personId")
    @Cascade({CascadeType.REFRESH})
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filmId")
    @Cascade(CascadeType.REFRESH)
    private Film film;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partType")
    @Cascade({CascadeType.REFRESH})
    private ParticipationValue partType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonParticipationInFilm that = (PersonParticipationInFilm) o;
        return Objects.equals(partId, that.partId) && person.equals(that.person) && film.equals(that.film) && partType.equals(that.partType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partId, person, film, partType);
    }
}
package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

@Entity
@Getter
@Setter
@Table(name = "PersonParticipationInFilm")
@NoArgsConstructor
public class PersonParticipationInFilm {
    public PersonParticipationInFilm(Person person, Film film, ParticipationValue partType) {
        this.parttype = partType;
        this.personid = person;
        this.filmid = film;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Cascade(org.hibernate.annotations.CascadeType.REFRESH)
    @JoinColumn(name = "personid", nullable = false)
    private Person personid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "filmid", nullable = false)
    private Film filmid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parttype", nullable = false)
    private ParticipationValue parttype;
}
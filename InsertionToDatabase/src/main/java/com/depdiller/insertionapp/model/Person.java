package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Person {
    @Id
    private Long personId;

    private String name;

    private LocalDate birthdate;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @ManyToMany
    @JoinTable(name = "BirthPlace",
            joinColumns = @JoinColumn(name = "personid"),
            inverseJoinColumns = @JoinColumn(name = "placeid"))
    private Set<Place> places = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "LinksToPerson",
            joinColumns = @JoinColumn(name = "personid"),
            inverseJoinColumns = @JoinColumn(name = "linkid"))
    private Set<WebsiteLink> websiteLinks = new HashSet<>();

    @OneToMany(mappedBy = "personid")
    private Set<PersonParticipationInFilm> personParticipationInFilms = new HashSet<>();

}
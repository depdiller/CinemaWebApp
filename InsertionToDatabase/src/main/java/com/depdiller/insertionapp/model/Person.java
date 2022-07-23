package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

    @ManyToMany(cascade = CascadeType.ALL)
    @Cascade({ org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    @JoinTable(name = "LinksToPerson",
            joinColumns = @JoinColumn(name = "personid"),
            inverseJoinColumns = @JoinColumn(name = "linkid"))
    private Set<WebsiteLink> websiteLinks = new HashSet<>();

    @OneToMany(mappedBy = "personid", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonParticipationInFilm> personParticipationInFilms = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.REFRESH)
    @JoinColumn(name = "birthplace")
    private Place birthPlace;
}
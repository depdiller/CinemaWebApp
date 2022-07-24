package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;

    private String name;
    private LocalDate birthdate;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @ManyToMany
    @JoinTable(name = "LinksToPerson",
            joinColumns = @JoinColumn(name = "personId"),
            inverseJoinColumns = @JoinColumn(name = "link"))
    @Cascade({CascadeType.REFRESH})
    private Set<WebsiteLink> websiteLinks = new HashSet<>();

    @OneToMany(mappedBy = "person")
    @Cascade({CascadeType.REFRESH})
    private Set<PersonParticipationInFilm> personParticipationInFilms = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "birthCity")
    @Cascade({CascadeType.REFRESH})
    private City birthCity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "birthCountry")
    @Cascade({CascadeType.REFRESH})
    private Country birthCountry;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(personId, person.personId) &&
                name.equals(person.name) &&
                Objects.equals(birthdate, person.birthdate) &&
                gender == person.gender &&
                Objects.equals(birthCity, person.birthCity) &&
                Objects.equals(birthCountry, person.birthCountry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, name, birthdate, gender, birthCity, birthCountry);
    }
}
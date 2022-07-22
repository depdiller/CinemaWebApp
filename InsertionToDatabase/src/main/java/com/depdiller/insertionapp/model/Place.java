package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placeid", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "birthplace")
    private Set<Person> people = new LinkedHashSet<>();

    public Place(City city, Country country) {
        this.cityname = city;
        this.countryname = country;
    }

//    @OneToMany(mappedBy = "birthPlace")
//    private Set<Person> people = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityname")
    private City cityname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryname")
    private Country countryname;

}

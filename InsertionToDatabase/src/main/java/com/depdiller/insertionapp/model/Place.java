package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

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

    @OneToMany(mappedBy = "birthPlace", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Person> people = new LinkedHashSet<>();

    public Place(City city, Country country) {
        this.cityname = city;
        this.countryname = country;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.REFRESH)
    @JoinColumn(name = "cityname")
    private City cityname;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.REFRESH)
    @JoinColumn(name = "countryname")
    private Country countryname;

}

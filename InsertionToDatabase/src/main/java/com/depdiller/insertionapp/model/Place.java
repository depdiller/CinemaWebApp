package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Place {
    @ManyToMany(mappedBy = "places")
    private Set<Person> people = new LinkedHashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placeid", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityname")
    private City cityname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryname")
    private Country countryname;

}

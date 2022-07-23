package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.SQLInsert;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLInsert(sql = "insert into country (countryname) values (?) on conflict do nothing")
public class Country {
    @Id
    private String countryName;
    public Country(String countryName) {
        this.countryName = countryName;
    }

    @OneToMany(mappedBy = "countryname", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Place> places = new HashSet<>();

    @ManyToMany(mappedBy = "countries")
    @Cascade({ org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    private Set<Film> films = new HashSet<>();
}

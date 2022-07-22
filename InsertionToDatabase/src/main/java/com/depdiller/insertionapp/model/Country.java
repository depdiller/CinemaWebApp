package com.depdiller.insertionapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;
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

    @OneToMany(mappedBy = "countryname")
    private Set<Place> places = new HashSet<>();

    @ManyToMany(mappedBy = "countries")
    private Set<Film> films = new HashSet<>();
}

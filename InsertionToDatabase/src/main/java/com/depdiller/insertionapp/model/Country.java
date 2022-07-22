package com.depdiller.insertionapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country {
    @Id
    private String countryName;
    public Country(String countryName) {
        this.countryName = countryName;
    }

    @OneToMany(mappedBy = "countryname")
    private Set<Place> places = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "countries")
    private Set<Film> films = new LinkedHashSet<>();

}

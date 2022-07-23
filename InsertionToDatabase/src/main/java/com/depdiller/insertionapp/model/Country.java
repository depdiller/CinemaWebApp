package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.SQLInsert;

import java.util.HashSet;
import java.util.Objects;
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

    @OneToMany(mappedBy = "country", orphanRemoval = true)
    @Cascade({CascadeType.REFRESH})
    private Set<Place> places = new HashSet<>();

    @ManyToMany(mappedBy = "countries")
    @Cascade({CascadeType.REFRESH})
    private Set<Film> films = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return countryName.equals(country.countryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryName);
    }
}

package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
//@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"cityName", "countryName"})})
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    public Place(City city, Country country) {
        this.city = city;
        this.country = country;
    }

    @OneToMany(mappedBy = "birthPlace")
    private Set<Person> people = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityName")
    @Cascade({CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryName")
    @Cascade({CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private Country country;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return city.equals(place.city) && country.equals(place.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, country);
    }
}

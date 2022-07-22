package com.depdiller.insertionapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@SQLInsert(sql = "insert into city (cityname) values (?) on conflict do nothing")
public class City {
    @Id
    private String cityName;
    public City(String  cityName) {
        this.cityName = cityName;
    }

    @OneToMany(mappedBy = "cityname")
    private Set<Place> places = new HashSet<>();
}

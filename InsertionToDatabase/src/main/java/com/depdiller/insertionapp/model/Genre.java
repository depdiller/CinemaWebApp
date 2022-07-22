package com.depdiller.insertionapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@SQLInsert(sql = "insert into genre (genre) values (?) on conflict do nothing")
public class Genre {
    @Id
    private String genre;
    public Genre(String genre) {
        this.genre = genre;
    }

    @ManyToMany(mappedBy = "genres")
    private Set<Film> films = new HashSet<>();

}

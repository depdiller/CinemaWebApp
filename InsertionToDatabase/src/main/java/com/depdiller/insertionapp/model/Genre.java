package com.depdiller.insertionapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
public class Genre {
    @Id
    private String genre;

    public Genre(String genre) {
        this.genre = genre;
    }

    @ManyToMany(mappedBy = "genres")
    @Cascade({CascadeType.REFRESH})
    private Set<Film> films = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre1 = (Genre) o;
        return genre.equals(genre1.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genre);
    }
}

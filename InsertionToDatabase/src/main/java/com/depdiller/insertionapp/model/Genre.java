package com.depdiller.insertionapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.LinkedHashSet;
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

    @ManyToMany(mappedBy = "genres")
    private Set<Film> films = new LinkedHashSet<>();

}

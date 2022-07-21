package com.depdiller.insertionapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationValue {
    @Id
    private String partType;

    @OneToMany(mappedBy = "parttype")
    private Set<PersonParticipationInFilm> personParticipationInFilms = new HashSet<>();
}

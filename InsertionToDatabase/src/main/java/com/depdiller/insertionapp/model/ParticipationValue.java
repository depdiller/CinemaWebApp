package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ParticipationValue")
public class ParticipationValue {
    @Id
    private String partType;
    public ParticipationValue(String partType) {
        this.partType = partType;
    }

    @OneToMany(mappedBy = "parttype", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonParticipationInFilm> personParticipationInFilms = new HashSet<>();
}
package com.depdiller.insertionapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @OneToMany(mappedBy = "parttype")
    private Set<PersonParticipationInFilm> personParticipationInFilms = new HashSet<>();
}
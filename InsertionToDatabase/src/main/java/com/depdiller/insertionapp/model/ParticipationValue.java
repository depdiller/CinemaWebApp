package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationValue {
    @Id
    private String partType;

    public ParticipationValue(String partType) {
        this.partType = partType;
    }

    @OneToMany(mappedBy = "partType")
    @Cascade(CascadeType.REFRESH)
    private Set<PersonParticipationInFilm> personParticipationInFilms = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipationValue that = (ParticipationValue) o;
        return partType.equals(that.partType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partType);
    }
}
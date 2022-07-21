package com.depdiller.insertionapp.model;

import jakarta.persistence.*;

@Entity
public class PersonParticipationInFilm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partId;

    @ManyToOne(fetch = FetchType.LAZY)
    private ParticipationValue partType;
}
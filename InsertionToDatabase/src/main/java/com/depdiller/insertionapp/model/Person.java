package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Person {
    @Id
    private Long personId;

    private String name;

    private LocalDate birthdate;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    private BirthPlace birthPlace;

    private Map<String, String> linkOnOtherWebsites;
}
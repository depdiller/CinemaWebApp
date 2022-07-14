package com.depdiller.insertionapp.model;

import lombok.Builder;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Builder
public class Person {
    private String name;
    private LocalDate birthdate;
    private Gender gender;
    private BirthPlace birthPlace;
}

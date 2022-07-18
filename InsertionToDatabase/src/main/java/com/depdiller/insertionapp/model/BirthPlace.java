package com.depdiller.insertionapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@NoArgsConstructor
public class BirthPlace {
    private String city;
    private String country;
}

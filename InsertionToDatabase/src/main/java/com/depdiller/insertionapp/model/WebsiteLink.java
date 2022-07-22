package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkId;

    private String link;

    @ManyToMany(mappedBy = "websiteLinks")
    private Set<Film> films = new HashSet<>();

    @ManyToMany(mappedBy = "websiteLinks")
    private Set<Person> people = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "websitename")
    private Website websitename;
}

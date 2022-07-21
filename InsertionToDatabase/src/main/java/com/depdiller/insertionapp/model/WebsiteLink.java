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
    @Column(name = "linkid", nullable = false)
    private Long linkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "websitename")
    private Website websiteName;

    @ManyToMany(mappedBy = "websiteLinks")
    private Set<Film> films = new HashSet<>();

    @ManyToMany(mappedBy = "websiteLinks")
    private Set<Person> people = new HashSet<>();
}

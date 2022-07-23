package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
    @Cascade({CascadeType.REFRESH})
    private Set<Film> films = new HashSet<>();

    @ManyToMany(mappedBy = "websiteLinks")
    @Cascade({CascadeType.REFRESH})
    private Set<Person> people = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.REFRESH)
    @JoinColumn(name = "websiteName")
    private Website website;
}

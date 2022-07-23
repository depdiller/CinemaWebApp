package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "filmid")
    private Long filmId;
    private String name;
    private String alternativeName;
    private String posterLink;
    private LocalDate worldPremier;
    private BigDecimal moneyEarnedWorldWide;

    @ManyToMany(cascade = CascadeType.ALL)
    @Cascade({ org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    @JoinTable(name = "LinksToFilm",
            joinColumns = @JoinColumn(name = "filmid"),
            inverseJoinColumns = @JoinColumn(name = "linkid"))
    private Set<WebsiteLink> websiteLinks = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @Cascade({ org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    @JoinTable(name = "FilmGenre",
            joinColumns = @JoinColumn(name = "filmid"),
            inverseJoinColumns = @JoinColumn(name = "genre"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @Cascade({ org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    @JoinTable(name = "FilmCountry",
            joinColumns = @JoinColumn(name = "filmid"),
            inverseJoinColumns = @JoinColumn(name = "countryname"))
    private Set<Country> countries = new HashSet<>();

    @Column(name = "durationMinutes")
    private Integer durationMinutes;

    @OneToMany(mappedBy = "partid", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonParticipationInFilm> personParticipationInFilms = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return filmId.equals(film.filmId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filmId);
    }
}

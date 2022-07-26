package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmId;

    private String name;
    private String alternativeName;
    private String posterLink;
    private LocalDate worldPremier;
    private BigDecimal moneyEarnedWorldWide;
    private Integer durationMinutes;

    @OneToMany(orphanRemoval = true, mappedBy = "link")
    @Cascade({CascadeType.REFRESH})
    Set<WebsiteLink> websiteLinks;

    @ManyToMany
    @Cascade({CascadeType.REFRESH})
    @JoinTable(name = "FilmGenre",
            joinColumns = @JoinColumn(name = "filmId"),
            inverseJoinColumns = @JoinColumn(name = "genre"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @Cascade({CascadeType.REFRESH})
    @JoinTable(name = "FilmCountry",
            joinColumns = @JoinColumn(name = "filmId"),
            inverseJoinColumns = @JoinColumn(name = "countryName"))
    private Set<Country> countries = new HashSet<>();

    @OneToMany(mappedBy = "partId", orphanRemoval = true)
    @Cascade({CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private List<PersonParticipationInFilm> personParticipationInFilms = new ArrayList<>();

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

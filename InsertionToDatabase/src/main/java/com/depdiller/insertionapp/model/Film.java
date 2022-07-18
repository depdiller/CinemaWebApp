package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Film {
    @Id
    @GeneratedValue
    private Long filmId;
    private String name;
    private String alternativeName;
    private String posterLink;
    private List<String> countries;
    private List<String> genres;
    private Map<String, String> linkOnOtherWebsites;
    private LocalDate worldPremier;
    private Integer duration;
    private BigDecimal moneyEarnedWorldWide;

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

package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteLink {
    @Id
    private String link;
    public WebsiteLink(String link) {
        this.link = link;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "LinksToFilm",
            joinColumns = {@JoinColumn(name = "link")},
            inverseJoinColumns = {@JoinColumn(name = "filmId")})
    @Cascade({CascadeType.REFRESH})
    private Film film;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "LinksToPerson",
            joinColumns = {@JoinColumn(name = "link")},
            inverseJoinColumns = {@JoinColumn(name = "personId")})
    @Cascade({CascadeType.REFRESH})
    private Person person;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebsiteLink that = (WebsiteLink) o;
        return link.equals(that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link);
    }
}

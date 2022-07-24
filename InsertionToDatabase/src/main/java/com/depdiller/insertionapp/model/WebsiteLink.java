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

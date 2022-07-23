package com.depdiller.insertionapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.SQLInsert;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLInsert(sql = "insert into website (websiteName) values (?) on conflict do nothing")
public class Website {
    @Id
    private String websiteName;

    public Website(String websiteName) {
        this.websiteName = websiteName;
    }

    @OneToMany(mappedBy = "website", orphanRemoval = true)
    @Cascade({CascadeType.REFRESH})
    private Set<WebsiteLink> websiteLinksAssociated= new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Website website = (Website) o;
        return websiteName.equals(website.websiteName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(websiteName);
    }
}
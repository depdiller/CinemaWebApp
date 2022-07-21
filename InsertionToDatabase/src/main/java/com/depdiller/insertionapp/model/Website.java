package com.depdiller.insertionapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Website {
    @Id
    private String websiteName;

    @OneToMany(mappedBy = "websitename")
    private Set<WebsiteLink> websiteLinks = new LinkedHashSet<>();

}

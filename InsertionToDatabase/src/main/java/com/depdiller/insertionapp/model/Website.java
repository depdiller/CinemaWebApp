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
public class Website {
    @Id
    private String websiteName;
    public Website(String websiteName) {
        this.websiteName = websiteName;
    }

    @OneToMany(mappedBy = "websitename", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WebsiteLink> websiteLinks = new HashSet<>();
}
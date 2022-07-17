package com.depdiller.insertionapp.model;

import lombok.Builder;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Builder
@ToString
public class Film {
    private String name;
    private String alternativeName;
    private String posterLink;
    private List<String> countries;
    private List<String> genres;
    private Map<String, String> linkOnOtherWebsites;
    private LocalDate worldPremier;
    private Integer duration;
    private BigDecimal moneyEarnedWorldWide;
}

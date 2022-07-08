package com.depdiller.insertionapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Builder
@ToString
public class Film {
    private String name;
    private String alternativeName;
    private String posterLink;
    private List<String> countries;
    private List<String> genres;
    private LocalDate worldPremier;
    private Integer duration;
    private BigDecimal moneyEarnedWorldWide;
}

package com.example.cinemawebapp.model;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class Film {
    private String name;
    private String alternativeName;
    private List<String> countries;
    private Date worldPremier;
    private Integer duration;
    private DecimalFormat moneyEarnedWorldWide;
}

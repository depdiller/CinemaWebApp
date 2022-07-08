package com.depdiller.insertionapp.model;

public enum Gender {
    male("male"), female("female");
    private final String asString;

    Gender(String asString) {
        this.asString = asString;
    }
    public String getAsString() {
        return asString;
    }
}

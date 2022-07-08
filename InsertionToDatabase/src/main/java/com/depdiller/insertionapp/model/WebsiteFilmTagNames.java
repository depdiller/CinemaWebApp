package com.depdiller.insertionapp.model;

public enum WebsiteFilmTagNames {
    alternativeName("Названия"), countries("Производство"),
    duration("Хронометраж"), genres("Жанр"),
    worldPremier("Первый показ"), moneyEarnedWorldWide("Мировые сборы");

    private final String russianTag;

    WebsiteFilmTagNames(String russianTag) {
        this.russianTag = russianTag;
    }

    public String getRussianTag() {
        return russianTag;
    }
}

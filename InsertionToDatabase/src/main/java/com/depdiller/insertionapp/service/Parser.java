package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.model.Person;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.NonNull;

public interface Parser {
    Film filmParse(HtmlPage page);
    Person personParse(HtmlPage page);
    void parseFilmCastAndAttachToFilmObject(Film film, HtmlPage filmPage);
}

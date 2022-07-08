package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.model.Film;
import com.depdiller.insertionapp.model.Person;
import com.depdiller.insertionapp.model.TvShow;

import java.text.ParseException;
import java.util.Map;

public interface Mapper<T, V> {
    Film filmMap(Map<T, V> filmData) throws ParseException;

    Person personMap(Map<T, V> personData);

    TvShow tvShowMap(Map<T, V> tvShowData);
}
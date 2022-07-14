package com.depdiller.insertionapp.model;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public enum Gender {
    male("мужской"), female("женский");
    private final String inRussian;
    private static Map<String, Gender> enumMap;

    Gender(String inRussian) {
        this.inRussian = inRussian;
    }

    public String getGenderInRussian() {
        return inRussian;
    }

    static {
        enumMap = new ConcurrentHashMap<>();
        for (var instance : Gender.values()) {
            enumMap.put(instance.getGenderInRussian(), instance);
        }
        enumMap = Collections.unmodifiableMap(enumMap);
    }

    public static Gender getGender(String genderInString) {
        return enumMap.get(genderInString);
    }
}

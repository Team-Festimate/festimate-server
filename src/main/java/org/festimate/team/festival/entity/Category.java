package org.festimate.team.festival.entity;

public enum Category {
    LIFE,
    MUSIC,
    SCHOOL;

    public static Category toCategory(String category) {
        return Category.valueOf(category.toUpperCase());
    }
}
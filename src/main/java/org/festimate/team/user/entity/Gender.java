package org.festimate.team.user.entity;

public enum Gender {
    MAN("남자"),
    WOMAN("여자");

    private final String description;

    Gender(String description) {
        this.description = description;
    }
}

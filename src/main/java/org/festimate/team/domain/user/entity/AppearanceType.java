package org.festimate.team.domain.user.entity;

public enum AppearanceType {
    DOG("강아지상"),
    CAT("고양이상"),
    RABBIT("토끼상"),
    FOX("여우상"),
    BEAR("곰상"),
    DINOSAUR("공룡상");

    private final String description;

    AppearanceType(String description) {
        this.description = description;
    }
}

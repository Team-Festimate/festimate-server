package org.festimate.team.domain.user.entity;

import lombok.Getter;

@Getter
public enum Mbti {
    INFP("INFP"),
    INTJ("INTJ"),
    ENTP("ENTP"),
    INFJ("INFJ"),
    ENFJ("ENFJ"),
    ISFP("ISFP"),
    ISTJ("ISTJ"),
    ESTJ("ESTJ"),
    ESFJ("ESFJ"),
    ISTP("ISTP"),
    ESTP("ESTP"),
    ISFJ("ISFJ"),
    INTP("INTP"),
    ENFP("ENFP"),
    ESFP("ESFP"),
    ENTJ("ENTJ");

    private final String description;

    Mbti(String description) {
        this.description = description;
    }
}
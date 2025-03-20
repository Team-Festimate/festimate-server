package org.festimate.team.participant.entity;

import lombok.Getter;

@Getter
public enum TypeResult {
    INFLUENCER("핵인싸메이트"),
    NEWBIE("뉴비메이트"),
    PHOTO("인증샷메이트"),
    PLANNER("계획파메이트"),
    HEALING("힐링메이트");

    private final String description;

    TypeResult(String description) {
        this.description = description;
    }
}
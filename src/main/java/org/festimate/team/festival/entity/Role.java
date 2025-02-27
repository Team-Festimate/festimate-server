package org.festimate.team.festival.entity;

import lombok.Getter;

@Getter
public enum Role {
    HOST("host"),
    PARTICIPANT("participant");

    private final String description;

    Role(String description) {
        this.description = description;
    }
}
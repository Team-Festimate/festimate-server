package org.festimate.team.domain.matching.entity;

import lombok.Getter;

@Getter
public enum MatchingStatus {
    COMPLETED("완료"),
    PENDING("보류");

    private final String description;

    MatchingStatus(String description) {
        this.description = description;
    }

}

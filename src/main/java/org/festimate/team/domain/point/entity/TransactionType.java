package org.festimate.team.domain.point.entity;

import lombok.Getter;

@Getter
public enum TransactionType {
    CREDIT("획득"),
    DEBIT("차감");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }
}
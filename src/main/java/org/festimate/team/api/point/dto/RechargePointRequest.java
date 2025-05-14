package org.festimate.team.api.point.dto;

import org.festimate.team.domain.point.entity.TransactionType;

public record RechargePointRequest(
        TransactionType type,
        long participantId,
        int point
) {
}

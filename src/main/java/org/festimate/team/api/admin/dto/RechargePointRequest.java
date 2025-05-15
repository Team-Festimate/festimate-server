package org.festimate.team.api.admin.dto;

import org.festimate.team.domain.point.entity.TransactionType;

public record RechargePointRequest(
        TransactionType type,
        long participantId,
        int point
) {
}

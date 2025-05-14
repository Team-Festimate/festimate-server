package org.festimate.team.api.point.dto;

public record RechargePointRequest(
        long participantId,
        int point
) {
}

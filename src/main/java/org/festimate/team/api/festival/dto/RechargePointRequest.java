package org.festimate.team.api.festival.dto;

public record RechargePointRequest(
        long participantId,
        int point
) {
}

package org.festimate.team.api.point.dto;

import java.util.List;

public record PointHistoryResponse(
        int totalPoint,
        List<PointHistory> histories
) {
    public static PointHistoryResponse from(int totalPoint, List<PointHistory> histories) {
        return new PointHistoryResponse(totalPoint, histories);
    }

}

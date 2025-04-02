package org.festimate.team.Point.dto;

import org.festimate.team.Point.entity.Point;
import org.festimate.team.Point.entity.TransactionType;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record PointHistory(
        TransactionType transactionType,
        int point,
        String date
) {
    public static PointHistory from(Point point) {
        String formattedDate = point.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        return new PointHistory(point.getTransactionType(), point.getPoint(), formattedDate);
    }

    public static List<PointHistory> from(List<Point> points) {
        return points.stream()
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .map(PointHistory::from)
                .toList();
    }
}

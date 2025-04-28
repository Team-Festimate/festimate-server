package org.festimate.team.api.festival.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FestivalRequest(
        String title,
        String category,
        LocalDate startDate,
        LocalDate endDate,
        LocalDateTime matchingStartAt
){}

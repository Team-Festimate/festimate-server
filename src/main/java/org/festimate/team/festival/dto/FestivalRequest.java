package org.festimate.team.festival.dto;

import java.time.LocalDate;

public record FestivalRequest(
        String title,
        String category,
        LocalDate startDate,
        LocalDate endDate
){}

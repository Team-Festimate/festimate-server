package org.festimate.team.festival.dto;

import org.festimate.team.festival.entity.Category;

import java.time.LocalDate;

public record FestivalRequest(
        String title,
        Category category,
        LocalDate startDate,
        LocalDate endDate
){}

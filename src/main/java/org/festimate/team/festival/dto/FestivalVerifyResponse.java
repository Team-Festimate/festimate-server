package org.festimate.team.festival.dto;

import org.festimate.team.festival.entity.Category;
import org.festimate.team.festival.entity.Festival;

public record FestivalVerifyResponse(
        Long festivalId,
        String title,
        Category category
) {
    public static FestivalVerifyResponse of(Festival festival) {
        return new FestivalVerifyResponse(
                festival.getFestivalId(),
                festival.getTitle(),
                festival.getCategory()
        );
    }
}

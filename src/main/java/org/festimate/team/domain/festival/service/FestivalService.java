package org.festimate.team.domain.festival.service;

import org.festimate.team.api.admin.dto.FestivalRequest;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.user.entity.User;

import java.util.List;

public interface FestivalService {
    Festival createFestival(User host, FestivalRequest request);

    Festival getFestivalByInviteCode(String inviteCode);

    Festival getFestivalByIdOrThrow(Long festivalId);

    List<Festival> getAllFestivals(User user);

    Festival getFestivalDetailByIdOrThrow(Long festivalId, User user);

    boolean isHost(User user, Festival festival);

    void validateCreateFestival(FestivalRequest request);
}

package org.festimate.team.festival.service;

import org.festimate.team.festival.dto.FestivalRequest;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.user.entity.User;

import java.util.List;

public interface FestivalService {
    Festival createFestival(User host, FestivalRequest request);

    Festival getFestivalByInviteCode(String inviteCode);

    List<Festival> getAllFestivals();

}

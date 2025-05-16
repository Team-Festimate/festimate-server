package org.festimate.team.domain.festivalHost.service;

import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface FestivalHostService {
    @Transactional
    void addHost(User newHost, Festival festival);
}

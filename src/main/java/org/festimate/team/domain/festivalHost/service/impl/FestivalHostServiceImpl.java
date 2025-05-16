package org.festimate.team.domain.festivalHost.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festivalHost.entity.FestivalHost;
import org.festimate.team.domain.festivalHost.repository.FestivalHostRepository;
import org.festimate.team.domain.festivalHost.service.FestivalHostService;
import org.festimate.team.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FestivalHostServiceImpl implements FestivalHostService {

    private final FestivalHostRepository festivalHostRepository;

    @Transactional
    @Override
    public void addHost(User newHost, Festival festival) {
        FestivalHost newFestivalHost = FestivalHost.builder()
                .festival(festival)
                .host(newHost)
                .build();

        festivalHostRepository.save(newFestivalHost);
    }
}

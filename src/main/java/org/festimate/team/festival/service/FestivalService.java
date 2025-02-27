package org.festimate.team.festival.service;

import lombok.RequiredArgsConstructor;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.repository.FestivalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FestivalService {

    private final FestivalRepository festivalRepository;

    @Transactional
    public Festival createFestival(Festival festival) {
        return festivalRepository.save(festival);
    }

    @Transactional(readOnly = true)
    public Festival getFestivalByInviteCode(String inviteCode) {
        return festivalRepository.findByInviteCode(inviteCode);
    }

    @Transactional(readOnly = true)
    public List<Festival> getAllFestivals() {
        return festivalRepository.findAll();
    }
}

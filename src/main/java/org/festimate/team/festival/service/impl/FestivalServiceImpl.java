package org.festimate.team.festival.service.impl;

import lombok.RequiredArgsConstructor;
import org.festimate.team.festival.dto.FestivalRequest;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.repository.FestivalRepository;
import org.festimate.team.festival.service.FestivalService;
import org.festimate.team.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class FestivalServiceImpl implements FestivalService {

    private final FestivalRepository festivalRepository;

    @Override
    @Transactional
    public Festival createFestival(User host, FestivalRequest request) {
        String inviteCode = generateUniqueInviteCode();

        Festival festival = Festival.builder()
                .host(host)
                .title(request.title())
                .category(request.category())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .inviteCode(inviteCode)
                .build();

        return festivalRepository.save(festival);
    }

    @Override
    @Transactional(readOnly = true)
    public Festival getFestivalByInviteCode(String inviteCode) {
        return festivalRepository.findByInviteCode(inviteCode);
    }

    @Override
    public List<Festival> getAllFestivals() {
        return festivalRepository.findAll();
    }

    private String generateUniqueInviteCode() {
        Random random = new Random();
        String inviteCode;
        do {
            inviteCode = String.valueOf(100000 + random.nextInt(900000));
        } while (festivalRepository.existsByInviteCode(inviteCode));
        return inviteCode;
    }
}

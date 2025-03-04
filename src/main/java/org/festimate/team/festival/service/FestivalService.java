package org.festimate.team.festival.service;

import lombok.RequiredArgsConstructor;
import org.festimate.team.festival.dto.FestivalRequest;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.entity.Participant;
import org.festimate.team.festival.entity.Role;
import org.festimate.team.festival.entity.TypeResult;
import org.festimate.team.festival.repository.FestivalRepository;
import org.festimate.team.festival.repository.ParticipantRepository;
import org.festimate.team.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class FestivalService {

    private final FestivalRepository festivalRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public Festival createFestival(FestivalRequest request) {
        String inviteCode = generateUniqueInviteCode();

        Festival festival = Festival.builder()
                .title(request.title())
                .category(request.category())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .inviteCode(inviteCode)
                .build();

        return festivalRepository.save(festival);
    }

    @Transactional
    public Participant applyFestival(User user, Festival festival, Role role) {
        Participant participant = Participant.builder()
                .user(user)
                .festival(festival)
                .role(role)
                .typeResult(TypeResult.HEALING)
                .introduction("")
                .message("")
                .build();

        return participantRepository.save(participant);
    }

    @Transactional(readOnly = true)
    public Festival getFestivalByInviteCode(String inviteCode) {
        return festivalRepository.findByInviteCode(inviteCode);
    }

    @Transactional(readOnly = true)
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

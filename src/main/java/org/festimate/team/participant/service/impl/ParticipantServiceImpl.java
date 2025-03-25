package org.festimate.team.participant.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.participant.dto.ProfileRequest;
import org.festimate.team.participant.entity.Participant;
import org.festimate.team.participant.repository.ParticipantRepository;
import org.festimate.team.participant.service.ParticipantService;
import org.festimate.team.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;

    @Override
    @Transactional
    public Participant createParticipant(User user, Festival festival, ProfileRequest request) {
        Participant participant = Participant.builder()
                .user(user)
                .festival(festival)
                .typeResult(request.typeResult())
                .introduction(request.introduction())
                .message(request.message())
                .build();

        return participantRepository.save(participant);
    }

    @Override
    public Participant getParticipant(User user, Festival festival) {
        return participantRepository.getParticipantByUserAndFestival(user, festival);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Festival> getFestivalsByUser(User user, String status) {
        return participantRepository.findAllByUser(user, status, LocalDate.now()).stream()
                .map(Participant::getFestival)
                .toList();
    }
}

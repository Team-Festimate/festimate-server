package org.festimate.team.participant.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.participant.dto.ProfileRequest;
import org.festimate.team.participant.entity.Participant;
import org.festimate.team.participant.repository.ParticipantRepository;
import org.festimate.team.participant.service.ParticipantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.festimate.team.user.entity.User;

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
                .typeResult(request.typeResult())  // 유형 테스트 결과
                .introduction(request.introduction())  // 자기소개
                .message(request.message())  // 상대에게 전달할 메시지
                .build();

        return participantRepository.save(participant);
    }

    @Override
    public boolean isAlreadyParticipant(User user, Festival festival) {
        return participantRepository.existsByUserAndFestival(user, festival);
    }
}

package org.festimate.team.participant.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.festival.dto.TypeRequest;
import org.festimate.team.festival.dto.TypeResponse;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.participant.dto.ProfileRequest;
import org.festimate.team.participant.entity.Participant;
import org.festimate.team.participant.entity.TypeResult;
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

    @Override
    public TypeResponse getTypeResult(TypeRequest typeRequest) {
        int[][] weights = new int[5][];
        weights[0] = typeRequest.q1() ? new int[]{3, 1, 1, -1, -1} : new int[]{-3, -1, -1, 1, 1};
        weights[1] = typeRequest.q2() ? new int[]{-2, 3, -1, 0, 0} : new int[]{2, -3, 1, 0, 0};
        weights[2] = typeRequest.q3() ? new int[]{-1, -2, 3, 1, -2} : new int[]{1, 2, -3, -1, 2};
        weights[3] = typeRequest.q4() ? new int[]{-1, -1, -2, 3, 1} : new int[]{1, 1, 2, -3, -1};
        weights[4] = typeRequest.q5() ? new int[]{-1, 0, -1, 0, 3} : new int[]{1, 0, 1, 0, -3};

        int[] totalScores = new int[5];

        for (int[] weight : weights) {
            for (int i = 0; i < 5; i++) {
                totalScores[i] += weight[i];
            }
        }

        int maxIdx = 0;
        for (int i = 1; i < 5; i++) {
            if (totalScores[i] > totalScores[maxIdx]) {
                maxIdx = i;
            }
        }

        return TypeResponse.from(TypeResult.values()[maxIdx]);
    }
}

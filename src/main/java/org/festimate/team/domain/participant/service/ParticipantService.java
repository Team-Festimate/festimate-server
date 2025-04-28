package org.festimate.team.domain.participant.service;


import org.festimate.team.api.participant.dto.TypeRequest;
import org.festimate.team.api.participant.dto.TypeResponse;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.api.participant.dto.ProfileRequest;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ParticipantService {
    @Transactional
    Participant createParticipant(User user, Festival festival, ProfileRequest request);

    Participant getParticipant(User user, Festival festival);

    Participant getParticipantById(Long participantId);

    @Transactional(readOnly = true)
    List<Festival> getFestivalsByUser(User user, String status);

    List<Participant> getParticipantByNickname(Festival festival, String nickname);

    TypeResponse getTypeResult(TypeRequest request);
}

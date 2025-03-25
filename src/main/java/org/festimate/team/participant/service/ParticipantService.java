package org.festimate.team.participant.service;


import org.festimate.team.festival.entity.Festival;
import org.festimate.team.participant.dto.ProfileRequest;
import org.festimate.team.participant.entity.Participant;
import org.festimate.team.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ParticipantService {
    @Transactional
    Participant createParticipant(User user, Festival festival, ProfileRequest request);

    Participant getParticipant(User user, Festival festival);

    @Transactional(readOnly = true)
    List<Festival> getFestivalsByUser(User user, String status);
}

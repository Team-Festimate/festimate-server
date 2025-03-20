package org.festimate.team.participant.service;


import org.festimate.team.festival.entity.Festival;
import org.festimate.team.participant.dto.ProfileRequest;
import org.festimate.team.participant.entity.Participant;
import org.festimate.team.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface ParticipantService {
    @Transactional
    Participant createParticipant(User user, Festival festival, ProfileRequest request);

    boolean isAlreadyParticipant(User user, Festival festival);
}

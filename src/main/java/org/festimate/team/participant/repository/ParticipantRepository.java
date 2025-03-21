package org.festimate.team.participant.repository;

import org.festimate.team.festival.entity.Festival;
import org.festimate.team.participant.entity.Participant;
import org.festimate.team.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    Participant getParticipantByUserAndFestival(User user, Festival festival);
}

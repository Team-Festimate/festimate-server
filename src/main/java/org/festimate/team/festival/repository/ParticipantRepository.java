package org.festimate.team.festival.repository;

import org.festimate.team.festival.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
}

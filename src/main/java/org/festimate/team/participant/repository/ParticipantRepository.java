package org.festimate.team.participant.repository;

import org.festimate.team.festival.entity.Festival;
import org.festimate.team.participant.entity.Participant;
import org.festimate.team.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Participant getParticipantByUserAndFestival(User user, Festival festival);

    @Query("SELECT p FROM Participant p WHERE p.user = :user AND " +
            "(:status = 'PROGRESS' AND p.festival.endDate >= :today OR " +
            ":status = 'END' AND p.festival.endDate < :today)")
    List<Participant> findAllByUser(User user, String status, LocalDate today);

    @Query("SELECT COALESCE(SUM(p.point), 0) FROM Point p WHERE p.participant = :participant")
    int getTotalPointByParticipant(@Param("participant") Participant participant);
}

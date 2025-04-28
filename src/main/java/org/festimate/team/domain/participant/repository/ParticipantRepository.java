package org.festimate.team.domain.participant.repository;

import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Participant getParticipantByUserAndFestival(User user, Festival festival);

    @Query("SELECT p FROM Participant p WHERE p.user = :user AND " +
            "(:status = 'PROGRESS' AND p.festival.endDate >= :today OR " +
            ":status = 'END' AND p.festival.endDate < :today)")
    List<Participant> findAllByUser(User user, String status, LocalDate today);

    List<Participant> findAllByFestivalAndUser_NicknameContaining(Festival festival, String nickname);
}

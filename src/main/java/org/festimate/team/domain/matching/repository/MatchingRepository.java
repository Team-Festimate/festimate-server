package org.festimate.team.domain.matching.repository;

import org.festimate.team.domain.matching.entity.Matching;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.entity.TypeResult;
import org.festimate.team.domain.user.entity.Gender;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long>, MatchingRepositoryCustom {
    @Query("""
                SELECT p FROM Participant p
                JOIN FETCH p.user
                LEFT JOIN Matching m1
                       ON (m1.applicantParticipant = p OR m1.targetParticipant = p)
                WHERE p.festival.festivalId = :festivalId
                  AND p.typeResult = :typeResult
                  AND p.user.gender != :gender
                  AND p.participantId != :participantId
                  AND p.participantId NOT IN (
                      SELECT m.targetParticipant.participantId
                      FROM Matching m
                      WHERE m.applicantParticipant.participantId = :participantId
                        AND m.status = 'COMPLETED'
                  )
                GROUP BY p
                ORDER BY COUNT(m1) ASC
            """)
    List<Participant> findMatchingCandidates(
            @Param("participantId") Long participantId,
            @Param("typeResult") TypeResult typeResult,
            @Param("gender") Gender gender,
            @Param("festivalId") Long festivalId,
            Pageable pageable
    );


    @Query("""
                SELECT m FROM Matching m
                WHERE m.festival.festivalId = :festivalId
                  AND m.status = 'PENDING'
                  AND m.applicantParticipant.user.gender != :gender
                ORDER BY m.createdAt ASC
            """)
    List<Matching> findAllPendingMatchingsByFestivalAndOppositeGender(
            @Param("festivalId") Long festivalId,
            @Param("gender") Gender gender
    );

    @Query("""
                SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END
                FROM Matching m
                WHERE m.applicantParticipant.participantId = :participantId
                  AND m.targetParticipant.participantId = :targetParticipantId
                  AND m.status = 'COMPLETED'
            """)
    boolean existsCompletedMatching(
            @Param("participantId") Long participantId,
            @Param("targetParticipantId") Long targetParticipantId
    );

    @Query("""
                SELECT m FROM Matching m
                WHERE m.applicantParticipant = :participant
                ORDER BY m.createdAt DESC
            """)
    List<Matching> findAllMatchingsByApplicantParticipant(Participant participant);

    @Query("SELECT COUNT(m) FROM Matching m WHERE m.applicantParticipant = :participant")
    int countAllByApplicant(@Param("participant") Participant participant);

    @Query("SELECT COUNT(m) FROM Matching m WHERE m.applicantParticipant = :participant AND m.status = 'COMPLETED'")
    int countCompletedByApplicant(@Param("participant") Participant participant);

    Optional<Matching> findByMatchingId(Long matchingId);
}


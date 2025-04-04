package org.festimate.team.matching.repository;

import org.festimate.team.matching.entity.Matching;
import org.festimate.team.participant.entity.Participant;
import org.festimate.team.participant.entity.TypeResult;
import org.festimate.team.user.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
    @Query("""
                SELECT p FROM Participant p
                WHERE p.festival.festivalId = :festivalId
                  AND p.typeResult = :typeResult
                  AND p.user.gender != :gender
                  AND p.participantId != :participantId
                  AND p.participantId NOT IN (
                      SELECT m.targetParticipant.participantId FROM Matching m
                      WHERE m.applicantParticipant.participantId = :participantId
                        AND m.status = 'COMPLETED'
                  )
                  AND (SIZE(p.matchingsAsTarget) + SIZE(p.matchingsAsApplicant)) = (
                      SELECT MIN(SIZE(p2.matchingsAsTarget) + SIZE(p2.matchingsAsApplicant))
                      FROM Participant p2
                      WHERE p2.festival.festivalId = :festivalId
                        AND p2.typeResult = :typeResult
                        AND p2.user.gender != :gender
                        AND p2.participantId != :participantId
                        AND p2.participantId NOT IN (
                            SELECT m2.targetParticipant.participantId FROM Matching m2
                            WHERE m2.applicantParticipant.participantId = :participantId
                              AND m2.status = 'COMPLETED'
                        )
                  )
            """)
    Optional<Participant> findMatchingCandidate(
            @Param("participantId") Long participantId,
            @Param("typeResult") TypeResult typeResult,
            @Param("gender") Gender gender,
            @Param("festivalId") Long festivalId
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

}


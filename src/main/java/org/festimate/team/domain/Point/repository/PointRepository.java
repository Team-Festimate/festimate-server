package org.festimate.team.domain.Point.repository;

import org.festimate.team.domain.Point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PointRepository extends JpaRepository<Point, Integer> {
    @Query(value = """
                SELECT COALESCE(SUM(
                    CASE 
                        WHEN p.transaction_type = 'CREDIT' THEN p.point
                        WHEN p.transaction_type = 'DEBIT' THEN -1 * p.point
                        ELSE 0
                    END
                ), 0)
                FROM point p
                WHERE p.participant_id = :participantId
            """, nativeQuery = true)
    int getTotalPointByParticipant(@Param("participantId") Long participantId);
}

package org.festimate.team.domain.matching.repository;

import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.entity.TypeResult;
import org.festimate.team.domain.user.entity.Gender;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MatchingRepositoryCustom {
    /**
     * QueryDSL로 매칭 후보를 동적으로 조회
     */
    List<Participant> findMatchingCandidatesDsl(
            Long applicantId,
            TypeResult typeResult,
            Gender gender,
            Long festivalId,
            Pageable pageable
    );
}

package org.festimate.team.domain.matching.repository;

import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.entity.TypeResult;
import org.festimate.team.domain.user.entity.Gender;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MatchingRepositoryCustom {
    List<Long> findExcludeIds(Long applicantId);

    List<Participant> findMatchingCandidatesDsl(
            Long applicantId,
            TypeResult typeResult,
            Gender gender,
            Long festivalId,
            Pageable pageable,
            List<Long> excludedIds
    );
}

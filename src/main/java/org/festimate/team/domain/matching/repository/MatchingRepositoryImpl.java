package org.festimate.team.domain.matching.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.festimate.team.domain.matching.entity.MatchingStatus;
import org.festimate.team.domain.matching.entity.QMatching;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.entity.QParticipant;
import org.festimate.team.domain.participant.entity.TypeResult;
import org.festimate.team.domain.user.entity.Gender;
import org.festimate.team.domain.user.entity.QUser;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class MatchingRepositoryImpl implements MatchingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QParticipant p = QParticipant.participant;
    private final QMatching m = QMatching.matching;
    private final QMatching mSub = new QMatching("mSub");
    private final QUser u = QUser.user;

    @Override
    public List<Participant> findMatchingCandidatesDsl(
            Long applicantId,
            TypeResult typeResult,
            Gender gender,
            Long festivalId,
            Pageable pageable
    ) {
        List<Long> excludedIds = queryFactory
                .select(mSub.targetParticipant.participantId)
                .from(mSub)
                .where(
                        mSub.applicantParticipant.participantId.eq(applicantId)
                                .and(mSub.status.eq(MatchingStatus.COMPLETED))
                )
                .fetch();

        return queryFactory
                .selectFrom(p)
                .join(p.user, u).fetchJoin()
                .leftJoin(m)
                .on(m.applicantParticipant.eq(p).or(m.targetParticipant.eq(p)))
                .where(
                        p.festival.festivalId.eq(festivalId),
                        p.typeResult.eq(typeResult),
                        u.gender.ne(gender),
                        p.participantId.ne(applicantId),
                        excludedIds.isEmpty() ? null : p.participantId.notIn(excludedIds)
                )
                .groupBy(p)
                .orderBy(m.count().asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}

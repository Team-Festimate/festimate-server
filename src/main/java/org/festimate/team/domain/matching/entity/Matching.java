package org.festimate.team.domain.matching.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.festimate.team.global.entity.BaseTimeEntity;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.participant.entity.Participant;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Entity
@DynamicUpdate
@Table(name = "matching")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Matching extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id", nullable = false)
    private Festival festival;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_participant_id", nullable = false)
    private Participant applicantParticipant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_participant_id")
    private Participant targetParticipant;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchingStatus status;

    @Column(nullable = false)
    private LocalDateTime matchDate;

    @Builder
    public Matching(Festival festival, Participant applicantParticipant, Participant targetParticipant,
                    MatchingStatus status, LocalDateTime matchDate) {
        this.festival = festival;
        this.applicantParticipant = applicantParticipant;
        this.targetParticipant = targetParticipant;
        this.status = status;
        this.matchDate = matchDate;
    }

    public void completeMatching(Participant targetParticipant) {
        this.targetParticipant = targetParticipant;
        this.status = MatchingStatus.COMPLETED;
        this.matchDate = LocalDateTime.now();
    }

}

package org.festimate.team.participant.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.festimate.team.Point.entity.Point;
import org.festimate.team.common.entity.BaseTimeEntity;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.matching.entity.Matching;
import org.festimate.team.user.entity.User;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Getter
@Entity
@DynamicUpdate
@Table(name = "participant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id", nullable = false)
    private Festival festival;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeResult typeResult;

    @Column(nullable = false)
    private String introduction;

    private String message;

    @OneToMany(mappedBy = "participant")
    private List<Point> points;

    @OneToMany(mappedBy = "applicantParticipant")
    private List<Matching> matchingsAsApplicant;

    @OneToMany(mappedBy = "targetParticipant")
    private List<Matching> matchingsAsTarget;

    @Builder
    public Participant(User user, Festival festival, TypeResult typeResult, String introduction, String message) {
        this.user = user;
        this.festival = festival;
        this.typeResult = typeResult;
        this.introduction = introduction;
        this.message = message;
    }
}

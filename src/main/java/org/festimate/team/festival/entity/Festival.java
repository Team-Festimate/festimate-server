package org.festimate.team.festival.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.festimate.team.common.entity.BaseTimeEntity;
import org.festimate.team.matching.entity.Matching;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
@DynamicUpdate
@Table(name = "festival")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Festival extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long festivalId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, unique = true)
    private String inviteCode;

    @OneToMany(mappedBy = "festival")
    private List<Participant> participants;

    @OneToMany(mappedBy = "festival")
    private List<Matching> matchings;

    @Builder
    public Festival(String title, Category category, LocalDate startDate, LocalDate endDate, String inviteCode) {
        this.title = title;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.inviteCode = inviteCode;
    }
}
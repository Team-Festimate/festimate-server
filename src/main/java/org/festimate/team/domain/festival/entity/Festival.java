package org.festimate.team.domain.festival.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.festimate.team.domain.festivalHost.entity.FestivalHost;
import org.festimate.team.domain.matching.entity.Matching;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.global.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalHost> festivalHosts = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalDateTime matchingStartAt;

    @Column(nullable = false, unique = true)
    private String inviteCode;

    @OneToMany(mappedBy = "festival")
    private List<Participant> participants;

    @OneToMany(mappedBy = "festival")
    private List<Matching> matchings;

    @Builder
    public Festival(String title, Category category, LocalDate startDate, LocalDate endDate, LocalDateTime matchingStartAt, String inviteCode) {
        this.title = title;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.matchingStartAt = matchingStartAt;
        this.inviteCode = inviteCode;
    }

    public FestivalStatus getFestivalStatus() {
        LocalDate now = LocalDate.now();
        LocalDate startDate = this.getStartDate();
        LocalDate endDate = this.getEndDate();

        if (now.isBefore(startDate)) {
            return FestivalStatus.BEFORE;
        } else if (now.isBefore(endDate.plusDays(1))) {
            return FestivalStatus.PROGRESS;
        } else if (now.isBefore(endDate.plusDays(8))) {
            return FestivalStatus.REFUND;
        } else {
            return FestivalStatus.END;
        }
    }

    public FestivalStatus getMatchingStartTimeStatus() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime matchingStartAt = this.matchingStartAt;

        if (now.isBefore(matchingStartAt)) {
            return FestivalStatus.BEFORE;
        } else return getFestivalStatus();
    }

    public void addHost(User user) {
        boolean alreadyExists = festivalHosts.stream()
                .anyMatch(fh -> fh.getHost().getUserId().equals(user.getUserId()));

        if (!alreadyExists) {
            FestivalHost host = FestivalHost.builder()
                    .festival(this)
                    .host(user)
                    .build();
            festivalHosts.add(host);
        }
    }

    public void removeHost(User user) {
        festivalHosts.removeIf(fh -> fh.getHost().equals(user));
    }

}
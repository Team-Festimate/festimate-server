package org.festimate.team.domain.festival.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.festimate.team.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "festival_host",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_festival_host_festival_user",
                        columnNames = {"festival_id", "user_id"}
                )
        }
)
public class FestivalHost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long festivalHostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id", nullable = false)
    private Festival festival;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User host;

    @Column(nullable = false)
    private LocalDateTime addedAt;

    @Builder
    public FestivalHost(Festival festival, User host) {
        this.festival = festival;
        this.host = host;
        this.addedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FestivalHost)) return false;
        FestivalHost that = (FestivalHost) o;
        return Objects.equals(getFestival(), that.getFestival()) &&
                Objects.equals(getHost(), that.getHost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFestival(), getHost());
    }

}

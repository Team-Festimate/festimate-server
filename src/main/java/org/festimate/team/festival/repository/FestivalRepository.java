package org.festimate.team.festival.repository;

import org.festimate.team.festival.entity.Festival;
import org.festimate.team.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FestivalRepository extends JpaRepository<Festival, Integer> {
    boolean existsByInviteCode(String inviteCode);

    Optional<Festival> findByInviteCode(String inviteCode);

    Optional<Festival> findByFestivalId(Long festivalId);

    List<Festival> findFestivalByHost(User host);
}

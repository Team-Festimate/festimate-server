package org.festimate.team.festival.repository;

import org.festimate.team.festival.entity.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FestivalRepository extends JpaRepository<Festival, Integer> {
    boolean existsByInviteCode(String inviteCode);

    Optional<Festival> findByInviteCode(String inviteCode);

    Optional<Festival> findByFestivalId(Long festivalId);
}

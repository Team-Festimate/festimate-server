package org.festimate.team.festival.repository;

import org.festimate.team.festival.entity.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository extends JpaRepository<Festival, Integer> {
    boolean existsByInviteCode(String inviteCode);

    Festival findByInviteCode(String inviteCode);
}

package org.festimate.team.festival.repository;

import org.festimate.team.festival.entity.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository extends JpaRepository<Festival, Integer> {
    Festival findByInviteCode(String inviteCode);  // 초대 코드로 조회하는 메서드 추가
}

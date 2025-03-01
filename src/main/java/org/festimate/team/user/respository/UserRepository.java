package org.festimate.team.user.respository;

import org.festimate.team.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByNickname(String nickname);
}

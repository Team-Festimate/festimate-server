package org.festimate.team.user.repository;

import org.festimate.team.user.entity.Platform;
import org.festimate.team.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByNickname(String nickname);

    @Query("SELECT u.nickname FROM User u WHERE u.userId = :userId")
    String findNicknameByUserId(Long userId);

    Optional<User> findByPlatformId(String platformId);

    User findByPlatformAndPlatformId(Platform platform, String platformId);

    Optional<User> getUserByUserId(Long userId);
}

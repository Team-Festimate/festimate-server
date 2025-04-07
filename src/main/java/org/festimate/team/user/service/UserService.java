package org.festimate.team.user.service;

import org.festimate.team.user.dto.SignUpRequest;
import org.festimate.team.user.entity.Platform;
import org.festimate.team.user.entity.User;

import java.util.Optional;

public interface UserService {
    void duplicateNickname(String nickname);

    Long getUserIdByPlatformId(String platformId);

    void updateRefreshToken(Long userId, String refreshToken);

    User saveUser(SignUpRequest request, String platformId);

    String getUserNickname(Long userId);

    User getUserById(Long userId);

    void validateRefreshToken(User user, String refreshToken);

    Optional<Long> getUserIdByPlatformAndPlatformId(Platform platform, String platformId);

    void findByIdOrThrow(Long userId);
}

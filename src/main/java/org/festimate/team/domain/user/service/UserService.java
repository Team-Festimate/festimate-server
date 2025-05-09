package org.festimate.team.domain.user.service;

import org.festimate.team.api.user.dto.SignUpRequest;
import org.festimate.team.domain.user.dto.UserInfoDto;
import org.festimate.team.domain.user.entity.Platform;
import org.festimate.team.domain.user.entity.User;

import java.util.Optional;

public interface UserService {
    void validateDuplicateNickname(String nickname);

    Long getUserIdByPlatformId(String platformId);

    void updateRefreshToken(Long userId, String refreshToken);

    User signUp(SignUpRequest request, String platformId);

    UserInfoDto getUserNicknameAndAppearanceType(Long userId);

    User getUserByIdOrThrow(Long userId);

    void validateRefreshToken(User user, String refreshToken);

    Optional<Long> getUserIdByPlatform(Platform platform, String platformId);
}

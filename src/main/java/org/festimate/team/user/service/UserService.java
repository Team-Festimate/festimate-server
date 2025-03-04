package org.festimate.team.user.service;

import org.festimate.team.user.dto.SignUpRequest;

public interface UserService {
    void duplicateNickname(String nickname);

    Long getUserIdByPlatformId(String platformId);

    void updateRefreshToken(Long userId, String refreshToken);

    Long saveUser(SignUpRequest request, String platformId);
}

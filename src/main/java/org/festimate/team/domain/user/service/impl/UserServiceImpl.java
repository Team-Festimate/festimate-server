package org.festimate.team.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.api.user.dto.SignUpRequest;
import org.festimate.team.domain.user.entity.Platform;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.domain.user.repository.UserRepository;
import org.festimate.team.domain.user.service.UserService;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.global.response.ResponseError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void validateDuplicateNickname(String nickname) {
        log.info("Checking nickname duplication: {}", nickname);
        if (userRepository.existsUserByNickname(nickname)) {
            throw new FestimateException(ResponseError.USER_ALREADY_EXISTS);
        }
    }

    @Transactional
    @Override
    public User signUp(SignUpRequest request, String platformId) {
        User user = User.builder()
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .nickname(request.nickName())
                .birthYear(request.birthYear())
                .gender(request.gender())
                .mbti(request.mbti())
                .appearanceType(request.appearanceType())
                .platformId(platformId)
                .platform(request.platform())
                .build();

        log.info("Checking user Info: {}", user);

        return userRepository.save(user);
    }

    @Override
    public String getUserNickname(Long userId) {
        getUserByIdOrThrow(userId);
        return userRepository.findNicknameByUserId(userId);
    }

    @Override
    public User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new FestimateException(ResponseError.USER_NOT_FOUND));
    }

    @Override
    public Long getUserIdByPlatformId(String platformId) {
        return userRepository.findByPlatformId(platformId).map(User::getUserId).orElse(null);
    }

    @Override
    @Transactional
    public void updateRefreshToken(Long userId, String refreshToken) {
        User user = getUserByIdOrThrow(userId);
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }

    @Override
    public void validateRefreshToken(User user, String refreshToken) {
        if (!user.validateRefreshToken(refreshToken))
            throw new FestimateException(ResponseError.INVALID_TOKEN);
    }

    @Override
    public Optional<Long> getUserIdByPlatform(Platform platform, String platformId) {
        return Optional.ofNullable(userRepository.findByPlatformAndPlatformId(platform, platformId))
                .map(User::getUserId);
    }
}

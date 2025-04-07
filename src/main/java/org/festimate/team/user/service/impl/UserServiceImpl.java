package org.festimate.team.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.common.response.ResponseError;
import org.festimate.team.exception.FestimateException;
import org.festimate.team.user.dto.SignUpRequest;
import org.festimate.team.user.entity.Platform;
import org.festimate.team.user.entity.User;
import org.festimate.team.user.repository.UserRepository;
import org.festimate.team.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void duplicateNickname(String nickname) {
        log.info("Checking nickname duplication: {}", nickname);
        if (userRepository.existsUserByNickname(nickname)) {
            throw new FestimateException(ResponseError.USER_ALREADY_EXISTS);
        }
    }

    @Transactional
    @Override
    public User saveUser(SignUpRequest request, String platformId) {
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
        findByIdOrThrow(userId);
        return userRepository.findNicknameByUserId(userId);
    }

    @Override
    public User getUserById(Long userId) {
        return findById(userId);
    }

    @Override
    public Long getUserIdByPlatformId(String platformId) {
        return userRepository.findByPlatformId(platformId).map(User::getUserId).orElse(null);
    }

    @Override
    @Transactional
    public void updateRefreshToken(Long userId, String refreshToken) {
        User user = findById(userId);
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }

    @Override
    public void validateRefreshToken(User user, String refreshToken) {
        if (!user.validateRefreshToken(refreshToken))
            throw new FestimateException(ResponseError.INVALID_TOKEN);
    }

    @Override
    public Optional<Long> getUserIdByPlatformAndPlatformId(Platform platform, String platformId) {
        return Optional.ofNullable(userRepository.findByPlatformAndPlatformId(platform, platformId))
                .map(User::getUserId);
    }

    @Override
    public void findByIdOrThrow(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new FestimateException(ResponseError.USER_NOT_FOUND));
    }

    private User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new FestimateException(ResponseError.USER_NOT_FOUND));
    }
}

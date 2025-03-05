package org.festimate.team.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.common.response.ResponseError;
import org.festimate.team.exception.FestimateException;
import org.festimate.team.user.dto.SignUpRequest;
import org.festimate.team.user.entity.User;
import org.festimate.team.user.repository.UserRepository;
import org.festimate.team.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long saveUser(SignUpRequest request, String platformId) {
        User user = User.builder()
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .nickname(request.nickName())
                .birthYear(request.birthYear())
                .mbti(request.mbti())
                .appearanceType(request.appearanceType())
                .platformId(platformId)
                .platform(request.platform())
                .build();

        log.info("Checking user Info: {}", user);

        return userRepository.save(user).getUserId();
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
    public void findByIdOrThrow(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new FestimateException(ResponseError.USER_NOT_FOUND));
    }

    private User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new FestimateException(ResponseError.USER_NOT_FOUND));
    }
}

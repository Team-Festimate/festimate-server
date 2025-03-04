package org.festimate.team.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.common.response.ResponseError;
import org.festimate.team.exception.FestimateException;
import org.festimate.team.user.dto.SignUpRequest;
import org.festimate.team.user.dto.SignUpResponse;
import org.festimate.team.user.entity.User;
import org.festimate.team.user.infra.KakaoApiClient;
import org.festimate.team.user.respository.UserRepository;
import org.festimate.team.user.security.JwtProvider;
import org.festimate.team.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final KakaoApiClient kakaoApiClient;


    @Override
    public void duplicateNickname(String nickname) {
        log.info("Checking nickname duplication: {}", nickname);
        if (userRepository.existsUserByNickname(nickname)) {
            throw new FestimateException(ResponseError.USER_ALREADY_EXISTS);
        }
    }

    @Override
    @Transactional
    public SignUpResponse signUp(String token, SignUpRequest request) {
        String platformId = kakaoApiClient.getPlatformId(token);

        findByPlatformId(platformId);
        duplicateNickname(request.nickName());

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

        userRepository.save(user);

        String accessToken = jwtProvider.createAccessToken(user.getUserId().toString());
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId().toString());

        return new SignUpResponse(user.getUserId(), accessToken, refreshToken);
    }

    @Override
    public Long getUserIdByPlatformId(String platformId) {
        return userRepository.findByPlatformId(platformId).map(User::getUserId).orElse(null);
    }

    @Override
    public void updateRefreshToken(Long userId, String refreshToken) {
        User user = userRepository.findById(userId).orElseThrow(() -> new FestimateException(ResponseError.USER_NOT_FOUND));
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }

    private User findByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new FestimateException(ResponseError.USER_NOT_FOUND));
    }


    private void findByPlatformId(String platformId) {
        if (userRepository.findByPlatformId(platformId).isPresent()) {
            throw new FestimateException(ResponseError.USER_ALREADY_EXISTS);
        }
    }
}

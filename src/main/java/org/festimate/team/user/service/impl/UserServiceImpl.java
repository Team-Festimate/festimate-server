package org.festimate.team.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.common.response.ResponseError;
import org.festimate.team.exception.FestimateException;
import org.festimate.team.user.respository.UserRepository;
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
}

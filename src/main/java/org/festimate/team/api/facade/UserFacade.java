package org.festimate.team.api.facade;

import lombok.RequiredArgsConstructor;
import org.festimate.team.global.response.ResponseError;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserFacade {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new FestimateException(ResponseError.USER_NOT_FOUND)
        );
    }
}

package org.festimate.team.domain.user.service.ipml;

import org.festimate.team.global.response.ResponseError;
import org.festimate.team.domain.user.service.impl.UserServiceImpl;
import org.festimate.team.domain.user.validator.NicknameValidator;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private NicknameValidator nicknameValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        nicknameValidator = new NicknameValidator();
    }

    @Test
    @DisplayName("닉네임_중복_검증_테스트 - 실패")
    void nicknameValidateDuplicateNicknameFail() {
        // given
        String nickname = "테스트닉네임";
        when(userRepository.existsUserByNickname(nickname)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.validateDuplicateNickname(nickname))
                .isInstanceOf(FestimateException.class)
                .hasMessageContaining(ResponseError.USER_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("닉네임_중복_검증_테스트 - 성공")
    void nicknameValidateDuplicateNicknameSuccess() {
        // given
        String nickname = "테스트닉네임";
        when(userRepository.existsUserByNickname(nickname)).thenReturn(false);

        // when & then (예외가 발생하지 않음을 검증)
        userService.validateDuplicateNickname(nickname);
        assertThat(userRepository.existsUserByNickname(nickname)).isFalse();
    }

    @Test
    @DisplayName("닉네임 길이 검증 - 초과")
    void nicknameValidatorLengthOver() {
        // given
        String longNickname = "테스트닉네임초과"; // 최대 길이 초과

        // when & then (길이 초과 시 예외 발생)
        assertThatThrownBy(() -> nicknameValidator.validate(longNickname))
                .isInstanceOf(FestimateException.class)
                .hasMessageContaining(ResponseError.INVALID_INPUT_LENGTH.getMessage());
    }

    @Test
    @DisplayName("닉네임 길이 검증 - 부족")
    void nicknameValidatorLengthUnder() {
        // given
        String shortNickname = "테"; // 최대 길이 초과

        assertThatThrownBy(() -> nicknameValidator.validate(shortNickname))
                .isInstanceOf(FestimateException.class)
                .hasMessageContaining(ResponseError.INVALID_INPUT_LENGTH.getMessage());
    }

    @Test
    @DisplayName("닉네임 한글 검증 - 한글이 아닌 경우")
    void nicknameValidatorKorea() {
        // given
        String numberNickname = "닉123";
        String emojiNickname = "😊😊";
        String consonantVowelNickname = "현진ㄴ";

        // when & then
        assertThatThrownBy(() -> nicknameValidator.validate(numberNickname))
                .isInstanceOf(FestimateException.class)
                .hasMessageContaining(ResponseError.INVALID_INPUT_NICKNAME.getMessage());

        assertThatThrownBy(() -> nicknameValidator.validate(emojiNickname))
                .isInstanceOf(FestimateException.class)
                .hasMessageContaining(ResponseError.INVALID_INPUT_NICKNAME.getMessage());

        assertThatThrownBy(() -> nicknameValidator.validate(consonantVowelNickname))
                .isInstanceOf(FestimateException.class)
                .hasMessageContaining(ResponseError.INVALID_INPUT_NICKNAME.getMessage());
    }
}

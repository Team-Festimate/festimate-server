package org.festimate.team.api.facade;

import org.festimate.team.api.auth.dto.TokenResponse;
import org.festimate.team.api.user.dto.SignUpRequest;
import org.festimate.team.common.mock.MockFactory;
import org.festimate.team.domain.user.entity.*;
import org.festimate.team.domain.user.service.UserService;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.global.response.ResponseError;
import org.festimate.team.infra.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class SignUpFacadeTest {

    @Mock
    private UserService userService;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private SignUpFacade signUpFacade;

    private SignUpRequest signUpRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        signUpRequest = new SignUpRequest(
                "이현진",
                "010-1234-5678",
                "현진",
                1999,
                Gender.MAN,
                Mbti.ENFJ,
                AppearanceType.BEAR,
                Platform.KAKAO
        );
    }

    @Test
    @DisplayName("닉네임 중복 검증 실패 - 예외 발생")
    void validateNickname_fail() {
        // given
        doThrow(new FestimateException(ResponseError.USER_ALREADY_EXISTS))
                .when(userService).validateDuplicateNickname("현진");

        // when & then
        assertThatThrownBy(() -> signUpFacade.validateNickname("현진"))
                .isInstanceOf(FestimateException.class)
                .hasMessageContaining(ResponseError.USER_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("회원가입 성공 - 토큰 발급")
    void signUp_success() {
        // given
        User user = MockFactory.mockUser("현진", Gender.WOMAN, 1L);

        when(userService.getUserIdByPlatformId("platformId")).thenReturn(null);
        when(userService.signUp(signUpRequest, "platformId")).thenReturn(user);
        when(jwtService.createAccessToken(1L)).thenReturn("access-token");
        when(jwtService.createRefreshToken(1L)).thenReturn("refresh-token");

        // when
        TokenResponse response = signUpFacade.signUp("platformId", signUpRequest);

        // then
        assertThat(response.userId()).isEqualTo(1L);
        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
    }
}

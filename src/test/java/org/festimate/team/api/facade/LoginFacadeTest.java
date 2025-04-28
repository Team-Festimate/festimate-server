package org.festimate.team.api.facade;

import org.festimate.team.api.auth.dto.TokenResponse;
import org.festimate.team.domain.auth.service.KakaoLoginService;
import org.festimate.team.domain.user.entity.Platform;
import org.festimate.team.domain.user.service.UserService;
import org.festimate.team.infra.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LoginFacadeTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private UserService userService;
    @Mock
    private KakaoLoginService kakaoLoginService;

    @InjectMocks
    private LoginFacade loginFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("기존 유저 로그인 성공")
    void login_existingUser_success() {
        // given
        when(userService.getUserIdByPlatform(Platform.KAKAO, "platformId"))
                .thenReturn(Optional.of(1L));

        when(jwtService.createAccessToken(1L)).thenReturn("access-token");
        when(jwtService.createRefreshToken(1L)).thenReturn("refresh-token");

        // when
        TokenResponse response = loginFacade.login("platformId", Platform.KAKAO);

        // then
        assertThat(response.userId()).isEqualTo(1L);
        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
    }

    @Test
    @DisplayName("신규 유저 임시 토큰 발급 성공")
    void login_newUser_tempToken_success() {
        // given
        when(userService.getUserIdByPlatform(Platform.KAKAO, "platformId"))
                .thenReturn(Optional.empty());

        when(jwtService.createTempAccessToken("platformId")).thenReturn("temp-access-token");
        when(jwtService.createTempRefreshToken("platformId")).thenReturn("temp-refresh-token");

        // when
        TokenResponse response = loginFacade.login("platformId", Platform.KAKAO);

        // then
        assertThat(response.userId()).isNull();
        assertThat(response.accessToken()).isEqualTo("temp-access-token");
        assertThat(response.refreshToken()).isEqualTo("temp-refresh-token");
    }
}

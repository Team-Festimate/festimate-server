package org.festimate.team.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.festimate.team.facade.FestivalFacade;
import org.festimate.team.facade.SignUpFacade;
import org.festimate.team.global.jwt.JwtService;
import org.festimate.team.global.jwt.TokenResponse;
import org.festimate.team.user.dto.SignUpRequest;
import org.festimate.team.user.dto.UserFestivalResponse;
import org.festimate.team.user.dto.UserNicknameResponse;
import org.festimate.team.user.service.UserService;
import org.festimate.team.user.validator.NicknameValidator;
import org.festimate.team.user.validator.UserRequestValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final NicknameValidator nicknameValidator;
    private final UserRequestValidator userRequestValidator;
    private final SignUpFacade signUpFacade;
    private final FestivalFacade festivalFacade;
    private final JwtService jwtService;

    @PostMapping("/validate-nickname")
    public ResponseEntity<ApiResponse<Void>> validateNickname(@RequestParam("nickname") String nickname) {
        nicknameValidator.validate(nickname);
        userService.duplicateNickname(nickname);

        return ResponseBuilder.ok(null);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<TokenResponse>> signUp(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody SignUpRequest request
    ) {
        log.info("Received Authorization Header: {}", accessToken);

        String platformId = jwtService.extractPlatformUserIdFromToken(accessToken);
        log.info("SignUp - platformId: {}", platformId);

        TokenResponse response = signUpFacade.signUp(platformId, request);
        return ResponseBuilder.created(response);
    }

    @GetMapping("/me/nickname")
    public ResponseEntity<ApiResponse<UserNicknameResponse>> getNickname(
            @RequestHeader("Authorization") String accessToken
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        String nickName = userService.getUserNickname(userId);
        return ResponseBuilder.ok(UserNicknameResponse.from(nickName));
    }

    @GetMapping("/me/festivals")
    public ResponseEntity<ApiResponse<List<UserFestivalResponse>>> getFestival(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam("status") String status
    ) {
        userRequestValidator.statusValidate(status);
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        return ResponseBuilder.ok(festivalFacade.getUserFestivals(userId, status));
    }
}

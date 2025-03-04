package org.festimate.team.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.auth.facade.AuthFacade;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.festimate.team.user.dto.SignUpRequest;
import org.festimate.team.user.dto.TokenResponse;
import org.festimate.team.user.service.UserService;
import org.festimate.team.user.validator.NicknameValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final NicknameValidator nicknameValidator;
    private final AuthFacade authFacade;

    @PostMapping("/validate/nickname")
    public ResponseEntity<ApiResponse<Void>> validateNickname(@RequestParam("nickname") String nickname) {
        nicknameValidator.validate(nickname);
        userService.duplicateNickname(nickname);

        return ResponseBuilder.ok(null);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> kakaoLogin(@RequestParam String code) {
        TokenResponse response = authFacade.login(code);
        return ResponseBuilder.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<TokenResponse>> signUp(
            @RequestHeader("Authorization") String token,
            @RequestBody SignUpRequest request
    ) {
        TokenResponse response = authFacade.signUp(token, request);
        return ResponseBuilder.created(response);
    }
}

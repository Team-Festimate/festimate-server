package org.festimate.team.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.festimate.team.user.dto.SignUpRequest;
import org.festimate.team.user.dto.SignUpResponse;
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

    @PostMapping("/validate/nickname")
    public ResponseEntity<ApiResponse<Void>> validateNickname(@RequestParam("nickname") String nickname) {
        nicknameValidator.validate(nickname);
        userService.duplicateNickname(nickname);

        return ResponseBuilder.ok(null);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(
            @RequestHeader("Authorization") String token,
            @RequestBody SignUpRequest request
    ) {
        SignUpResponse response = userService.signUp(token, request);
        return ResponseBuilder.created(response);
    }
}

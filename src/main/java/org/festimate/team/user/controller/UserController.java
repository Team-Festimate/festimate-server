package org.festimate.team.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.festimate.team.user.validator.NicknameValidator;
import org.festimate.team.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}

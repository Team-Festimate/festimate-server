package org.festimate.team.user.controller;

import lombok.RequiredArgsConstructor;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.festimate.team.user.dto.TokenResponse;
import org.festimate.team.user.facade.AuthFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> kakaoLogin(@RequestParam String code) {
        TokenResponse response = authFacade.kakaoLogin(code);
        return ResponseBuilder.ok(response);
    }
}

package org.festimate.team.auth.controller;

import lombok.RequiredArgsConstructor;
import org.festimate.team.auth.dto.TokenResponse;
import org.festimate.team.auth.service.AuthService;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> kakaoLogin(@RequestParam String code) {
        TokenResponse response = authService.kakaoLogin(code);
        return ResponseBuilder.ok(response);
    }
}

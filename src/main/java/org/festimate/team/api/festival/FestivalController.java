package org.festimate.team.api.festival;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.facade.FestivalFacade;
import org.festimate.team.api.festival.dto.*;
import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.festimate.team.infra.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/festivals")
@RequiredArgsConstructor
public class FestivalController {
    private final JwtService jwtService;
    private final FestivalFacade festivalFacade;

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<FestivalVerifyResponse>> verifyFestival(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody FestivalVerifyRequest request
    ) {
        jwtService.parseTokenAndGetUserId(accessToken);
        FestivalVerifyResponse response = festivalFacade.verifyFestival(request);
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}")
    public ResponseEntity<ApiResponse<FestivalInfoResponse>> getFestivalInfo(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        FestivalInfoResponse response = festivalFacade.getFestivalInfo(userId, festivalId);
        return ResponseBuilder.ok(response);
    }
}

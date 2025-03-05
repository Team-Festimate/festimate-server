package org.festimate.team.festival.controller;

import lombok.RequiredArgsConstructor;
import org.festimate.team.auth.jwt.JwtProvider;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.festimate.team.facade.FestivalFacade;
import org.festimate.team.festival.dto.FestivalRequest;
import org.festimate.team.festival.dto.FestivalResponse;
import org.festimate.team.festival.dto.FestivalVerifyRequest;
import org.festimate.team.festival.dto.FestivalVerifyResponse;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.service.FestivalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/festival")
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;
    private final FestivalFacade festivalFacade;
    private final JwtProvider jwtProvider;

    @PostMapping
    public ResponseEntity<ApiResponse<FestivalResponse>> createFestival(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody FestivalRequest request
    ) {
        Long userId = jwtProvider.parseTokenAndGetUserId(accessToken);
        FestivalResponse response = festivalFacade.createFestival(userId, request);

        return ResponseBuilder.created(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<FestivalVerifyResponse>> verifyFestival(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody FestivalVerifyRequest request
    ) {
        jwtProvider.parseTokenAndGetUserId(accessToken);
        Festival festival = festivalService.getFestivalByInviteCode(request.inviteCode().trim());

        return ResponseBuilder.ok(FestivalVerifyResponse.of(festival));
    }

    @GetMapping
    public ResponseEntity<List<Festival>> getAllFestivals() {
        List<Festival> festivals = festivalService.getAllFestivals();
        return ResponseEntity.ok(festivals);
    }
}

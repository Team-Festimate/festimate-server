package org.festimate.team.festival.controller;

import lombok.RequiredArgsConstructor;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.festimate.team.facade.FestivalFacade;
import org.festimate.team.festival.dto.*;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.service.FestivalService;
import org.festimate.team.global.jwt.JwtService;
import org.festimate.team.participant.dto.ProfileRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/festival")
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;
    private final FestivalFacade festivalFacade;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<ApiResponse<FestivalResponse>> createFestival(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody FestivalRequest request
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        FestivalResponse response = festivalFacade.createFestival(userId, request);

        return ResponseBuilder.created(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<FestivalVerifyResponse>> verifyFestival(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody FestivalVerifyRequest request
    ) {
        jwtService.parseTokenAndGetUserId(accessToken);
        Festival festival = festivalService.getFestivalByInviteCode(request.inviteCode().trim());

        return ResponseBuilder.ok(FestivalVerifyResponse.of(festival));
    }

    @PostMapping("/{festivalId}/entry")
    public ResponseEntity<ApiResponse<EntryResponse>> entryFestival(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @RequestBody ProfileRequest request
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);

        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);

        EntryResponse response = festivalFacade.enterFestival(userId, festival, request);

        return ResponseBuilder.created(response);
    }

    @GetMapping
    public ResponseEntity<List<Festival>> getAllFestivals() {
        List<Festival> festivals = festivalService.getAllFestivals();
        return ResponseEntity.ok(festivals);
    }
}

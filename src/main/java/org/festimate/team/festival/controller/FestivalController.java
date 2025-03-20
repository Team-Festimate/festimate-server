package org.festimate.team.festival.controller;

import lombok.RequiredArgsConstructor;
import org.festimate.team.auth.jwt.JwtProvider;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.festimate.team.facade.FestivalFacade;
import org.festimate.team.festival.dto.*;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.service.FestivalService;
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

    @PostMapping("/{festivalId}/entry")
    public ResponseEntity<ApiResponse<EntryResponse>> entryFestival(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @RequestBody ProfileRequest request
    ) {
        // 유저의 액세스 토큰이 유효한지 확인
        Long userId = jwtProvider.parseTokenAndGetUserId(accessToken);
        // 유효한 페스티벌 아이디인지 확인
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);

        // 유효한 리퀘스트인지 확인하기
        // request

        // 이미 페스티벌에 참가하는 참가자인지 확인하고 아니라면 입장시키기
        EntryResponse response = festivalFacade.entryFestival(userId, festival, request);

        return ResponseBuilder.created(response);
    }

    @GetMapping
    public ResponseEntity<List<Festival>> getAllFestivals() {
        List<Festival> festivals = festivalService.getAllFestivals();
        return ResponseEntity.ok(festivals);
    }
}

package org.festimate.team.festival.controller;

import lombok.RequiredArgsConstructor;
import org.festimate.team.Point.dto.PointHistoryResponse;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.festimate.team.facade.FestivalFacade;
import org.festimate.team.facade.UserFacade;
import org.festimate.team.festival.dto.*;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.service.FestivalService;
import org.festimate.team.global.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminFestivalController {
    private final FestivalService festivalService;
    private final FestivalFacade festivalFacade;
    private final UserFacade userFacade;
    private final JwtService jwtService;

    @PostMapping("/festival")
    public ResponseEntity<ApiResponse<FestivalResponse>> createFestival(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody FestivalRequest request
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        FestivalResponse response = festivalFacade.createFestival(userId, request);

        return ResponseBuilder.created(response);
    }

    @GetMapping("/festivals")
    public ResponseEntity<ApiResponse<List<AdminFestivalResponse>>> getAllFestivals(
            @RequestHeader("Authorization") String accessToken
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);

        List<Festival> festivals = festivalService.getAllFestivals(
                userFacade.getUserById(userId)
        );

        List<AdminFestivalResponse> response = festivals.stream()
                .map(AdminFestivalResponse::of)
                .toList();
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/festivals/{festivalId}")
    public ResponseEntity<ApiResponse<AdminFestivalDetailResponse>> getFestivalDetail(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        AdminFestivalDetailResponse response
                = AdminFestivalDetailResponse.of(festivalService.getFestivalDetailByIdOrThrow(festivalId, userId));

        return ResponseBuilder.ok(response);
    }

    @GetMapping("/festivals/{festivalId}/participants/{participantId}/points")
    public ResponseEntity<ApiResponse<PointHistoryResponse>> getParticipantPointHistory(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @PathVariable("participantId") Long participantId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);

        PointHistoryResponse response = festivalFacade.getParticipantPointHistory(userId, festivalId, participantId);

        return ResponseBuilder.ok(response);
    }

    @GetMapping("/festivals/{festivalId}/participants/search")
    public ResponseEntity<ApiResponse<List<SearchParticipantResponse>>> getParticipantByNickname(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @RequestParam("nickname") String nickname
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);

        List<SearchParticipantResponse> response = festivalFacade.getParticipantByNickname(userId, festivalId, nickname);
        return ResponseBuilder.ok(response);
    }

    @PostMapping("/festival/{festivalId}/points")
    public ResponseEntity<ApiResponse<FestivalResponse>> rechargePoints(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @RequestBody RechargePointRequest request
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        festivalFacade.rechargePoints(userId, festivalId, request);

        return ResponseBuilder.created(null);
    }
}

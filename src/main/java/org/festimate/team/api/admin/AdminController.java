package org.festimate.team.api.admin;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.admin.dto.*;
import org.festimate.team.api.facade.FestivalFacade;
import org.festimate.team.api.facade.ParticipantFacade;
import org.festimate.team.api.facade.PointFacade;
import org.festimate.team.api.point.dto.PointHistoryResponse;
import org.festimate.team.api.point.dto.RechargePointRequest;
import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.festimate.team.infra.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/festivals")
@RequiredArgsConstructor
public class AdminController {
    private final JwtService jwtService;
    private final FestivalFacade festivalFacade;
    private final ParticipantFacade participantFacade;
    private final PointFacade pointFacade;

    @PostMapping()
    public ResponseEntity<ApiResponse<FestivalResponse>> createFestival(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody FestivalRequest request
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        FestivalResponse response = festivalFacade.createFestival(userId, request);
        return ResponseBuilder.created(response);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<AdminFestivalResponse>>> getAllFestivals(
            @RequestHeader("Authorization") String accessToken
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        List<AdminFestivalResponse> response = festivalFacade.getAllFestivals(userId);
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}")
    public ResponseEntity<ApiResponse<AdminFestivalDetailResponse>> getFestivalDetail(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        AdminFestivalDetailResponse response = festivalFacade.getFestivalDetail(userId, festivalId);
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}/participants/search")
    public ResponseEntity<ApiResponse<List<SearchParticipantResponse>>> getParticipantByNickname(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @RequestParam("nickname") String nickname
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);

        List<SearchParticipantResponse> response = participantFacade.getParticipantByNickname(userId, festivalId, nickname);
        return ResponseBuilder.ok(response);
    }

    @PostMapping("/{festivalId}/points")
    public ResponseEntity<ApiResponse<Void>> rechargePoints(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @RequestBody RechargePointRequest request
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        pointFacade.rechargePoints(userId, festivalId, request);
        return ResponseBuilder.ok(null);
    }

    @GetMapping("/{festivalId}/participants/{participantId}/points")
    public ResponseEntity<ApiResponse<PointHistoryResponse>> getParticipantPointHistory(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @PathVariable("participantId") Long participantId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        PointHistoryResponse response = pointFacade.getParticipantPointHistory(userId, festivalId, participantId);
        return ResponseBuilder.ok(response);
    }
}

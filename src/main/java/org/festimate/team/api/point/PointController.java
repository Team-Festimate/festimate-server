package org.festimate.team.api.point;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.facade.PointFacade;
import org.festimate.team.api.point.dto.PointHistoryResponse;
import org.festimate.team.api.point.dto.RechargePointRequest;
import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.festimate.team.infra.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PointController {

    private final JwtService jwtService;
    private final PointFacade pointFacade;

    @GetMapping("/festivals/{festivalId}/me/points")
    public ResponseEntity<ApiResponse<PointHistoryResponse>> getMyPointHistory(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        PointHistoryResponse response = pointFacade.getMyPointHistory(userId, festivalId);
        return ResponseBuilder.ok(response);
    }

    @PostMapping("/admin/festival/{festivalId}/points")
    public ResponseEntity<ApiResponse<Void>> rechargePoints(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @RequestBody RechargePointRequest request
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        pointFacade.rechargePoints(userId, festivalId, request);
        return ResponseBuilder.created(null);
    }

    @GetMapping("/admin/festivals/{festivalId}/participants/{participantId}/points")
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
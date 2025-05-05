package org.festimate.team.api.point;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.facade.PointFacade;
import org.festimate.team.api.point.dto.PointHistoryResponse;
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

    @GetMapping("/festivals/{festivalId}/participants/me/points")
    public ResponseEntity<ApiResponse<PointHistoryResponse>> getMyPointHistory(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        PointHistoryResponse response = pointFacade.getMyPointHistory(userId, festivalId);
        return ResponseBuilder.ok(response);
    }
}
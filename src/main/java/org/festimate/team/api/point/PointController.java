package org.festimate.team.api.point;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.facade.PointFacade;
import org.festimate.team.api.point.dto.PointHistoryResponse;
import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PointController {

    private final PointFacade pointFacade;

    @GetMapping("/festivals/{festivalId}/participants/me/points")
    public ResponseEntity<ApiResponse<PointHistoryResponse>> getMyPointHistory(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId
    ) {
        PointHistoryResponse response = pointFacade.getMyPointHistory(userId, festivalId);
        return ResponseBuilder.ok(response);
    }
}
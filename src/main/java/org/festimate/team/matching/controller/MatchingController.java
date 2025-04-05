package org.festimate.team.matching.controller;

import lombok.RequiredArgsConstructor;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.festimate.team.facade.FestivalFacade;
import org.festimate.team.global.jwt.JwtService;
import org.festimate.team.matching.dto.MatchingListResponse;
import org.festimate.team.matching.dto.MatchingStatusResponse;
import org.festimate.team.matching.service.MatchingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/festivals")
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;
    private final FestivalFacade festivalFacade;
    private final JwtService jwtService;

    @PostMapping("/{festivalId}/matchings")
    public ResponseEntity<ApiResponse<MatchingStatusResponse>> createMatching(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);

        MatchingStatusResponse response = festivalFacade.createMatching(userId, festivalId);
        return ResponseBuilder.created(response);
    }

    @GetMapping("/{festivalId}/matchings")
    public ResponseEntity<ApiResponse<MatchingListResponse>> getMatching(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);

        MatchingListResponse response = festivalFacade.getMatchingList(userId, festivalId);
        return ResponseBuilder.ok(response);
    }
}

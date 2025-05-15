package org.festimate.team.api.matching;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.facade.MatchingFacade;
import org.festimate.team.api.matching.dto.MatchingDetailInfo;
import org.festimate.team.api.matching.dto.MatchingListResponse;
import org.festimate.team.api.matching.dto.MatchingStatusResponse;
import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.festimate.team.infra.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/festivals")
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingFacade matchingFacade;
    private final JwtService jwtService;

    @PostMapping("/{festivalId}/matchings")
    public ResponseEntity<ApiResponse<MatchingStatusResponse>> createMatching(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);

        MatchingStatusResponse response = matchingFacade.createMatching(userId, festivalId);
        return ResponseBuilder.created(response);
    }

    @GetMapping("/{festivalId}/matchings")
    public ResponseEntity<ApiResponse<MatchingListResponse>> getMatching(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);

        MatchingListResponse response = matchingFacade.getMatchingList(userId, festivalId);
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}/matchings/{matchingId}")
    public ResponseEntity<ApiResponse<MatchingDetailInfo>> getMatchingDetail(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @PathVariable("matchingId") Long matchingId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);

        MatchingDetailInfo response = matchingFacade.getMatchingDetail(userId, festivalId, matchingId);
        return ResponseBuilder.ok(response);
    }
}

package org.festimate.team.api.matching;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.facade.MatchingFacade;
import org.festimate.team.api.matching.dto.MatchingDetailInfo;
import org.festimate.team.api.matching.dto.MatchingListResponse;
import org.festimate.team.api.matching.dto.MatchingStatusResponse;
import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/festivals")
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingFacade matchingFacade;

    @PostMapping("/{festivalId}/matchings")
    public ResponseEntity<ApiResponse<MatchingStatusResponse>> createMatching(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId
    ) {
        MatchingStatusResponse response = matchingFacade.createMatching(userId, festivalId);
        return ResponseBuilder.created(response);
    }

    @GetMapping("/{festivalId}/matchings")
    public ResponseEntity<ApiResponse<MatchingListResponse>> getMatching(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId
    ) {
        MatchingListResponse response = matchingFacade.getMatchingList(userId, festivalId);
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}/matchings/{matchingId}")
    public ResponseEntity<ApiResponse<MatchingDetailInfo>> getMatchingDetail(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId,
            @PathVariable("matchingId") Long matchingId
    ) {
        MatchingDetailInfo response = matchingFacade.getMatchingDetail(userId, festivalId, matchingId);
        return ResponseBuilder.ok(response);
    }
}

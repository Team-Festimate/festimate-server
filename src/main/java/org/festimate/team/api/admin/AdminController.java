package org.festimate.team.api.admin;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.admin.dto.*;
import org.festimate.team.api.facade.*;
import org.festimate.team.api.point.dto.PointHistoryResponse;
import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/festivals")
@RequiredArgsConstructor
public class AdminController {
    private final FestivalFacade festivalFacade;
    private final FestivalHostFacade festivalHostFacade;
    private final ParticipantFacade participantFacade;
    private final PointFacade pointFacade;
    private final MatchingFacade matchingFacade;

    @PostMapping()
    public ResponseEntity<ApiResponse<FestivalResponse>> createFestival(
            @RequestAttribute("userId") Long userId,
            @RequestBody FestivalRequest request
    ) {
        FestivalResponse response = festivalFacade.createFestival(userId, request);
        return ResponseBuilder.created(response);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<AdminFestivalResponse>>> getAllFestivals(
            @RequestAttribute("userId") Long userId
    ) {
        List<AdminFestivalResponse> response = festivalFacade.getAllFestivals(userId);
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}")
    public ResponseEntity<ApiResponse<AdminFestivalDetailResponse>> getFestivalDetail(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long festivalId
    ) {
        AdminFestivalDetailResponse response = festivalFacade.getFestivalDetail(userId, festivalId);
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}/participants/search")
    public ResponseEntity<ApiResponse<List<SearchParticipantResponse>>> getParticipantByNickname(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId,
            @RequestParam("nickname") String nickname
    ) {
        List<SearchParticipantResponse> response = participantFacade.getParticipantByNickname(userId, festivalId, nickname);
        return ResponseBuilder.ok(response);
    }

    @PostMapping("/{festivalId}/points")
    public ResponseEntity<ApiResponse<Void>> rechargePoints(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId,
            @RequestBody RechargePointRequest request
    ) {
        pointFacade.rechargePoints(userId, festivalId, request);
        return ResponseBuilder.ok(null);
    }

    @PostMapping("/{festivalId}/hosts")
    public ResponseEntity<ApiResponse<Void>> addHost(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId,
            @RequestBody AddHostRequest request
    ) {
        festivalHostFacade.addHost(userId, festivalId, request);
        return ResponseBuilder.created(null);
    }

    @GetMapping("/{festivalId}/participants/{participantId}/points")
    public ResponseEntity<ApiResponse<PointHistoryResponse>> getParticipantPointHistory(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId,
            @PathVariable("participantId") Long participantId
    ) {
        PointHistoryResponse response = pointFacade.getParticipantPointHistory(userId, festivalId, participantId);
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}/participants/{participantId}/matchings")
    public ResponseEntity<ApiResponse<AdminMatchingResponse>> getParticipantMatchingHistory(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId,
            @PathVariable("participantId") Long participantId
    ) {
        AdminMatchingResponse response = matchingFacade.getMatchingSize(userId, festivalId, participantId);
        return ResponseBuilder.ok(response);
    }
}

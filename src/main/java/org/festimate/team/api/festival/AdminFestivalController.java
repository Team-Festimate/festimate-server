package org.festimate.team.api.festival;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.facade.FestivalFacade;
import org.festimate.team.api.facade.UserFacade;
import org.festimate.team.api.festival.dto.*;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.festimate.team.infra.jwt.JwtService;
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
}

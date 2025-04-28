package org.festimate.team.api.festival;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.festival.dto.*;
import org.festimate.team.api.point.dto.PointHistoryResponse;
import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.festimate.team.api.facade.FestivalFacade;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.infra.jwt.JwtService;
import org.festimate.team.api.participant.dto.ProfileRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/festivals")
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;
    private final FestivalFacade festivalFacade;
    private final JwtService jwtService;

    @PostMapping("/verify-code")
    public ResponseEntity<ApiResponse<FestivalVerifyResponse>> verifyFestival(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody FestivalVerifyRequest request
    ) {
        jwtService.parseTokenAndGetUserId(accessToken);
        Festival festival = festivalService.getFestivalByInviteCode(request.inviteCode().trim());

        return ResponseBuilder.ok(FestivalVerifyResponse.of(festival));
    }

    @PostMapping("/{festivalId}/entry")
    public ResponseEntity<ApiResponse<EntryResponse>> entryFestival(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);
        EntryResponse response = festivalFacade.enterFestival(userId, festival);

        return ResponseBuilder.ok(response);
    }

    @PostMapping("/{festivalId}/participants")
    public ResponseEntity<ApiResponse<EntryResponse>> createParticipant(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @RequestBody ProfileRequest request
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);

        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);

        EntryResponse response = festivalFacade.createParticipant(userId, festival, request);

        return ResponseBuilder.created(response);
    }

    @GetMapping("/{festivalId}")
    public ResponseEntity<ApiResponse<FestivalInfoResponse>> getFestivalInfo(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);

        festivalFacade.validateUserParticipation(userId, festival);
        return ResponseBuilder.ok(FestivalInfoResponse.of(festival));
    }

    @GetMapping("/{festivalId}/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);

        ProfileResponse response = festivalFacade.getParticipantProfile(userId, festival);
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}/me/summary")
    public ResponseEntity<ApiResponse<MainUserInfoResponse>> getParticipantAndPoint(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);

        MainUserInfoResponse response = festivalFacade.getParticipantAndPoint(userId, festival);

        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}/me/type")
    public ResponseEntity<ApiResponse<DetailProfileResponse>> getParticipantType(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);

        DetailProfileResponse response = festivalFacade.getParticipantType(userId, festival);

        return ResponseBuilder.ok(response);
    }

    @PatchMapping("/{festivalId}/me/message")
    public ResponseEntity<ApiResponse<Void>> modifyMyMessage(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @RequestBody MessageRequest request
    ){
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);

        festivalFacade.modifyMyMessage(userId, festival, request);

        return ResponseBuilder.created(null);
    }

    @GetMapping("/{festivalId}/me/points")
    public ResponseEntity<ApiResponse<PointHistoryResponse>> getMyPointHistory(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);

        PointHistoryResponse response = festivalFacade.getMyPointHistory(userId, festival);

        return ResponseBuilder.ok(response);
    }
}

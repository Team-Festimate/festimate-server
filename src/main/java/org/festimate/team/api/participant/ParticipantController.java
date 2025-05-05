package org.festimate.team.api.participant;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.facade.ParticipantFacade;
import org.festimate.team.api.participant.dto.*;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.festimate.team.infra.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/festivals")
@RequiredArgsConstructor
public class ParticipantController {

    private final JwtService jwtService;
    private final ParticipantService participantService;
    private final ParticipantFacade participantFacade;

    @PostMapping("/{festivalId}/participants/type")
    public ResponseEntity<ApiResponse<TypeResponse>> getFestivalType(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long festivalId,
            @RequestBody TypeRequest request
    ) {
        jwtService.parseTokenAndGetUserId(accessToken);
        TypeResponse response = participantService.getTypeResult(request);
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}/participants/me")
    public ResponseEntity<ApiResponse<EntryResponse>> entryFestival(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        EntryResponse response = participantFacade.entryFestival(userId, festivalId);

        return ResponseBuilder.ok(response);
    }

    @PostMapping("/{festivalId}/participants")
    public ResponseEntity<ApiResponse<EntryResponse>> createParticipant(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @RequestBody ProfileRequest request
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        EntryResponse response = participantFacade.createParticipant(userId, festivalId, request);
        return ResponseBuilder.created(response);
    }

    @GetMapping("/{festivalId}/participants/me/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        ProfileResponse response = participantFacade.getParticipantProfile(userId, festivalId);
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}/participants/me/summary")
    public ResponseEntity<ApiResponse<MainUserInfoResponse>> getParticipantAndPoint(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        MainUserInfoResponse response = participantFacade.getParticipantSummary(userId, festivalId);

        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}/participants/me/type")
    public ResponseEntity<ApiResponse<DetailProfileResponse>> getParticipantType(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        DetailProfileResponse response = participantFacade.getParticipantType(userId, festivalId);

        return ResponseBuilder.ok(response);
    }

    @PatchMapping("/{festivalId}/participants/me/message")
    public ResponseEntity<ApiResponse<Void>> modifyMyMessage(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @RequestBody MessageRequest request
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        participantFacade.modifyMessage(userId, festivalId, request);

        return ResponseBuilder.created(null);
    }
}

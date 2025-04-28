package org.festimate.team.api.participant;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.facade.ParticipantFacade;
import org.festimate.team.api.festival.dto.*;
import org.festimate.team.api.participant.dto.ProfileRequest;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.festimate.team.infra.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ParticipantController {

    private final JwtService jwtService;
    private final ParticipantService participantService;
    private final ParticipantFacade participantFacade;

    @PostMapping("/participants/type")
    public ResponseEntity<ApiResponse<TypeResponse>> getFestivalType(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody TypeRequest request
    ) {
        jwtService.parseTokenAndGetUserId(accessToken);

        TypeResponse response = participantService.getTypeResult(request);
        return ResponseBuilder.ok(response);
    }

    @PostMapping("/festivals/{festivalId}/entry")
    public ResponseEntity<ApiResponse<EntryResponse>> entryFestival(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        EntryResponse response = participantFacade.entryFestival(userId, festivalId);

        return ResponseBuilder.ok(response);
    }

    @PostMapping("/festivals/{festivalId}/participants")
    public ResponseEntity<ApiResponse<EntryResponse>> createParticipant(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @RequestBody ProfileRequest request
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        EntryResponse response = participantFacade.createParticipant(userId, festivalId, request);
        return ResponseBuilder.created(response);
    }

    @GetMapping("/festivals/{festivalId}/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        ProfileResponse response = participantFacade.getParticipantProfile(userId, festivalId);
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/festivals/{festivalId}/me/summary")
    public ResponseEntity<ApiResponse<MainUserInfoResponse>> getParticipantAndPoint(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        MainUserInfoResponse response = participantFacade.getParticipantSummary(userId, festivalId);

        return ResponseBuilder.ok(response);
    }

    @GetMapping("/festivals/{festivalId}/me/type")
    public ResponseEntity<ApiResponse<DetailProfileResponse>> getParticipantType(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        DetailProfileResponse response = participantFacade.getParticipantType(userId, festivalId);

        return ResponseBuilder.ok(response);
    }

    @PatchMapping("/festivals/{festivalId}/me/message")
    public ResponseEntity<ApiResponse<Void>> modifyMyMessage(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @RequestBody MessageRequest request
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        participantFacade.modifyMessage(userId, festivalId, request);

        return ResponseBuilder.created(null);
    }

    @GetMapping("/admin/festivals/{festivalId}/participants/search")
    public ResponseEntity<ApiResponse<List<SearchParticipantResponse>>> getParticipantByNickname(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId,
            @RequestParam("nickname") String nickname
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);

        List<SearchParticipantResponse> response = participantFacade.getParticipantByNickname(userId, festivalId, nickname);
        return ResponseBuilder.ok(response);
    }
}

package org.festimate.team.api.participant;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.facade.ParticipantFacade;
import org.festimate.team.api.participant.dto.*;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/festivals")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;
    private final ParticipantFacade participantFacade;

    @PostMapping("/{festivalId}/participants/type")
    public ResponseEntity<ApiResponse<TypeResponse>> getFestivalType(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long festivalId,
            @RequestBody TypeRequest request
    ) {
        TypeResponse response = participantService.getTypeResult(request);
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}/participants/me")
    public ResponseEntity<ApiResponse<EntryResponse>> entryFestival(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId
    ) {
        EntryResponse response = participantFacade.entryFestival(userId, festivalId);

        return ResponseBuilder.ok(response);
    }

    @PostMapping("/{festivalId}/participants")
    public ResponseEntity<ApiResponse<EntryResponse>> createParticipant(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId,
            @RequestBody ProfileRequest request
    ) {
        EntryResponse response = participantFacade.createParticipant(userId, festivalId, request);
        return ResponseBuilder.created(response);
    }

    @GetMapping("/{festivalId}/participants/me/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId
    ) {
        ProfileResponse response = participantFacade.getParticipantProfile(userId, festivalId);
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}/participants/me/summary")
    public ResponseEntity<ApiResponse<MainUserInfoResponse>> getParticipantAndPoint(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId
    ) {
        MainUserInfoResponse response = participantFacade.getParticipantSummary(userId, festivalId);

        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}/participants/me/type")
    public ResponseEntity<ApiResponse<DetailProfileResponse>> getParticipantType(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId
    ) {
        DetailProfileResponse response = participantFacade.getParticipantType(userId, festivalId);

        return ResponseBuilder.ok(response);
    }

    @PatchMapping("/{festivalId}/participants/me/message")
    public ResponseEntity<ApiResponse<Void>> modifyMyMessage(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId,
            @RequestBody MessageRequest request
    ) {
        participantFacade.modifyMessage(userId, festivalId, request);

        return ResponseBuilder.created(null);
    }
}

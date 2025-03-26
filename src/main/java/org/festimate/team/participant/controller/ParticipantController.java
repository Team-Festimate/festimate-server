package org.festimate.team.participant.controller;

import lombok.RequiredArgsConstructor;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.festimate.team.festival.dto.TypeRequest;
import org.festimate.team.festival.dto.TypeResponse;
import org.festimate.team.global.jwt.JwtService;
import org.festimate.team.participant.service.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/participant")
@RequiredArgsConstructor
public class ParticipantController {

    private final JwtService jwtService;
    private final ParticipantService participantService;

    @PostMapping("/type")
    public ResponseEntity<ApiResponse<TypeResponse>> getFestivalType(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody TypeRequest request
    ) {
        jwtService.parseTokenAndGetUserId(accessToken);

        TypeResponse response = participantService.getTypeResult(request);
        return ResponseBuilder.ok(response);
    }
}

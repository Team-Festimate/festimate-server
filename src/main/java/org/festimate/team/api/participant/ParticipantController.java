package org.festimate.team.api.participant;

import lombok.RequiredArgsConstructor;
import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.festimate.team.api.festival.dto.TypeRequest;
import org.festimate.team.api.festival.dto.TypeResponse;
import org.festimate.team.infra.jwt.JwtService;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/participants")
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

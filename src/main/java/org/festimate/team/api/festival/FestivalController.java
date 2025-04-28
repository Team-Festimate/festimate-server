package org.festimate.team.api.festival;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.facade.ParticipantFacade;
import org.festimate.team.api.festival.dto.FestivalInfoResponse;
import org.festimate.team.api.festival.dto.FestivalVerifyRequest;
import org.festimate.team.api.festival.dto.FestivalVerifyResponse;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.festimate.team.infra.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/festivals")
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;
    private final ParticipantFacade participantFacade;
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

    @GetMapping("/{festivalId}")
    public ResponseEntity<ApiResponse<FestivalInfoResponse>> getFestivalInfo(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("festivalId") Long festivalId
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);

        participantFacade.getParticipant(userId, festivalId);
        return ResponseBuilder.ok(FestivalInfoResponse.of(festival));
    }
}

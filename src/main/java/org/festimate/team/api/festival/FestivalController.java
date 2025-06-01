package org.festimate.team.api.festival;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.facade.FestivalFacade;
import org.festimate.team.api.festival.dto.FestivalInfoResponse;
import org.festimate.team.api.festival.dto.FestivalVerifyRequest;
import org.festimate.team.api.festival.dto.FestivalVerifyResponse;
import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/festivals")
@RequiredArgsConstructor
public class FestivalController {
    private final FestivalFacade festivalFacade;

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<FestivalVerifyResponse>> verifyFestival(
            @RequestAttribute("userId") Long userId,
            @RequestBody FestivalVerifyRequest request
    ) {
        FestivalVerifyResponse response = festivalFacade.verifyFestival(userId, request);
        return ResponseBuilder.ok(response);
    }

    @GetMapping("/{festivalId}")
    public ResponseEntity<ApiResponse<FestivalInfoResponse>> getFestivalInfo(
            @RequestAttribute("userId") Long userId,
            @PathVariable("festivalId") Long festivalId
    ) {
        FestivalInfoResponse response = festivalFacade.getFestivalInfo(userId, festivalId);
        return ResponseBuilder.ok(response);
    }
}

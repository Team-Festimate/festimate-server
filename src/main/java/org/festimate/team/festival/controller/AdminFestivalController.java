package org.festimate.team.festival.controller;

import lombok.RequiredArgsConstructor;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.festimate.team.facade.FestivalFacade;
import org.festimate.team.festival.dto.FestivalRequest;
import org.festimate.team.festival.dto.FestivalResponse;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.service.FestivalService;
import org.festimate.team.global.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminFestivalController {
    private final FestivalService festivalService;
    private final FestivalFacade festivalFacade;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<ApiResponse<FestivalResponse>> createFestival(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody FestivalRequest request
    ) {
        Long userId = jwtService.parseTokenAndGetUserId(accessToken);
        FestivalResponse response = festivalFacade.createFestival(userId, request);

        return ResponseBuilder.created(response);
    }

    @GetMapping
    public ResponseEntity<List<Festival>> getAllFestivals() {
        List<Festival> festivals = festivalService.getAllFestivals();
        return ResponseEntity.ok(festivals);
    }
}

package org.festimate.team.festival.controller;

import lombok.RequiredArgsConstructor;
import org.festimate.team.auth.jwt.JwtProvider;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.festimate.team.festival.dto.FestivalRequest;
import org.festimate.team.festival.dto.FestivalResponse;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.entity.Role;
import org.festimate.team.festival.service.FestivalService;
import org.festimate.team.user.entity.User;
import org.festimate.team.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/festival")
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<FestivalResponse>> createFestival(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody FestivalRequest request
    ) {
        Long userId = jwtProvider.parseTokenAndGetUserId(accessToken);
        User user = userService.getUserById(userId);

        Festival festival = festivalService.createFestival(request);
        festivalService.applyFestival(user, festival, Role.HOST);

        return ResponseBuilder.created(FestivalResponse.from(festival.getFestivalId(), festival.getInviteCode()));
    }

    @GetMapping("/{inviteCode}")
    public ResponseEntity<Festival> getFestivalByInviteCode(@PathVariable String inviteCode) {
        Festival festival = festivalService.getFestivalByInviteCode(inviteCode);
        return ResponseEntity.ok(festival);
    }

    @GetMapping
    public ResponseEntity<List<Festival>> getAllFestivals() {
        List<Festival> festivals = festivalService.getAllFestivals();
        return ResponseEntity.ok(festivals);
    }
}

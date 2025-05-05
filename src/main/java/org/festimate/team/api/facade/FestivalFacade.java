package org.festimate.team.api.facade;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.admin.dto.AdminFestivalDetailResponse;
import org.festimate.team.api.admin.dto.AdminFestivalResponse;
import org.festimate.team.api.admin.dto.FestivalRequest;
import org.festimate.team.api.admin.dto.FestivalResponse;
import org.festimate.team.api.festival.dto.FestivalInfoResponse;
import org.festimate.team.api.festival.dto.FestivalVerifyRequest;
import org.festimate.team.api.festival.dto.FestivalVerifyResponse;
import org.festimate.team.api.user.dto.UserFestivalResponse;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.domain.user.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FestivalFacade {
    private final UserService userService;
    private final FestivalService festivalService;
    private final ParticipantService participantService;

    @Transactional(readOnly = true)
    public FestivalVerifyResponse verifyFestival(FestivalVerifyRequest request) {
        Festival festival = festivalService.getFestivalByInviteCode(request.inviteCode().trim());
        return FestivalVerifyResponse.of(festival);
    }

    @Transactional(readOnly = true)
    public FestivalInfoResponse getFestivalInfo(Long userId, Long festivalId) {
        User user = userService.getUserById(userId);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);
        participantService.validateParticipation(user, festival);
        return FestivalInfoResponse.of(festival);
    }

    @Transactional
    public FestivalResponse createFestival(Long userId, FestivalRequest request) {
        User host = userService.getUserById(userId);

        festivalService.validateCreateFestival(request);

        Festival festival = festivalService.createFestival(host, request);
        return FestivalResponse.from(festival.getFestivalId(), festival.getInviteCode());
    }

    @Transactional(readOnly = true)
    public List<UserFestivalResponse> getUserFestivals(Long userId, String status) {
        User user = userService.getUserById(userId);
        return participantService.getFestivalsByUser(user, status)
                .stream()
                .map(UserFestivalResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AdminFestivalResponse> getAllFestivals(Long userId) {
        User user = userService.getUserById(userId);
        List<Festival> festivals = festivalService.getAllFestivals(user);
        return festivals.stream()
                .map(AdminFestivalResponse::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdminFestivalDetailResponse getFestivalDetail(Long userId, Long festivalId) {
        Festival festival = festivalService.getFestivalDetailByIdOrThrow(festivalId, userId);
        return AdminFestivalDetailResponse.of(festival);
    }

}

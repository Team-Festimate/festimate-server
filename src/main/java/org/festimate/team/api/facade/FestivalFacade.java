package org.festimate.team.api.facade;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.festival.dto.FestivalRequest;
import org.festimate.team.api.festival.dto.FestivalResponse;
import org.festimate.team.api.user.dto.UserFestivalResponse;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.domain.user.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.festimate.team.domain.festival.validator.DateValidator.isFestivalDateValid;
import static org.festimate.team.domain.festival.validator.DateValidator.isMatchingStartTimeValid;
import static org.festimate.team.domain.festival.validator.FestivalRequestValidator.isFestivalValid;

@Component
@RequiredArgsConstructor
public class FestivalFacade {
    private final UserService userService;
    private final FestivalService festivalService;
    private final ParticipantService participantService;

    public FestivalResponse createFestival(Long userId, FestivalRequest request) {
        User host = userService.getUserById(userId);

        isFestivalValid(request.title(), request.category());
        isFestivalDateValid(request.startDate(), request.endDate());
        isMatchingStartTimeValid(request.startDate(), request.matchingStartAt());

        Festival festival = festivalService.createFestival(host, request);

        return FestivalResponse.from(festival.getFestivalId(), festival.getInviteCode());
    }

    @Transactional(readOnly = true)
    public List<UserFestivalResponse> getUserFestivals(Long userId, String status) {
        User user = userService.getUserById(userId);
        return participantService.getFestivalsByUser(user, status).stream()
                .map(UserFestivalResponse::from)
                .toList();
    }
}

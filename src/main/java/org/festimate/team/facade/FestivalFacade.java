package org.festimate.team.facade;

import lombok.RequiredArgsConstructor;
import org.festimate.team.festival.dto.FestivalRequest;
import org.festimate.team.festival.dto.FestivalResponse;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.service.FestivalService;
import org.festimate.team.user.entity.User;
import org.festimate.team.user.service.UserService;
import org.springframework.stereotype.Component;

import static org.festimate.team.festival.validator.DateValidator.isFestivalDateValid;
import static org.festimate.team.festival.validator.FestivalRequestValidator.isFestivalValid;

@Component
@RequiredArgsConstructor
public class FestivalFacade {
    private final UserService userService;
    private final FestivalService festivalService;

    public FestivalResponse createFestival(Long userId, FestivalRequest request) {
        User host = userService.getUserById(userId);

        isFestivalValid(request.title(), request.category());
        isFestivalDateValid(request.startDate(), request.endDate());

        Festival festival = festivalService.createFestival(host, request);

        return FestivalResponse.from(festival.getFestivalId(), festival.getInviteCode());
    }
}

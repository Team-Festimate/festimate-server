package org.festimate.team.api.facade;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.admin.dto.AddHostRequest;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.domain.festivalHost.service.FestivalHostService;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.domain.user.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FestivalHostFacade {
    private final UserService userService;
    private final FestivalService festivalService;
    private final FestivalHostService festivalHostService;
    private final ParticipantService participantService;

    @Transactional
    public void addHost(Long userId, Long festivalId, AddHostRequest request) {
        User host = userService.getUserByIdOrThrow(userId);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);
        festivalService.isHost(host, festival);

        Participant participant = participantService.getParticipantById(request.participantId());
        User newHost = participant.getUser();
        festivalHostService.addHost(newHost, festival);
    }
}

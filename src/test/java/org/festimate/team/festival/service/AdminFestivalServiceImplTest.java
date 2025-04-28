package org.festimate.team.festival.service;

import org.festimate.team.domain.Point.service.PointService;
import org.festimate.team.common.mock.MockFactory;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.api.facade.FestivalFacade;
import org.festimate.team.api.festival.dto.RechargePointRequest;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.festimate.team.domain.user.entity.Gender;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdminFestivalServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private FestivalService festivalService;
    @Mock
    private ParticipantService participantService;
    @Mock
    private PointService pointService;

    @InjectMocks
    private FestivalFacade festivalFacade;

    private User host;
    private User targetUser;
    private Festival festival;
    private Participant participant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        host = MockFactory.mockUser("호스트", Gender.MAN, 1L);
        targetUser = MockFactory.mockUser("참가자", Gender.WOMAN, 2L);
        festival = MockFactory.mockFestival(host, 100L, LocalDate.now(), LocalDate.now().plusDays(2));
        participant = MockFactory.mockParticipant(targetUser, festival, null, 200L);
    }

    @Test
    @DisplayName("포인트 충전 - 정상 케이스")
    void rechargePoints_success() {
        // given
        RechargePointRequest request = new RechargePointRequest(participant.getParticipantId(), 5);

        when(userService.getUserById(1L)).thenReturn(host);
        when(festivalService.getFestivalByIdOrThrow(100L)).thenReturn(festival);
        when(festivalService.isHost(host, festival)).thenReturn(true);
        when(participantService.getParticipantById(200L)).thenReturn(participant);

        // when
        festivalFacade.rechargePoints(1L, 100L, request);

        // then
        verify(pointService).rechargePoint(participant, 5);
    }

    @Test
    @DisplayName("포인트 충전 - 요청자가 호스트가 아님")
    void rechargePoints_notHost_throws() {
        User attacker = MockFactory.mockUser("악당", Gender.MAN, 999L);
        RechargePointRequest request = new RechargePointRequest(participant.getParticipantId(), 5);

        when(userService.getUserById(999L)).thenReturn(attacker);
        when(festivalService.getFestivalByIdOrThrow(100L)).thenReturn(festival);
        when(festivalService.isHost(attacker, festival)).thenReturn(false);

        assertThatThrownBy(() ->
                festivalFacade.rechargePoints(999L, 100L, request)
        ).isInstanceOf(FestimateException.class);
    }

    @Test
    @DisplayName("포인트 충전 - 참가자가 해당 페스티벌 소속 아님")
    void rechargePoints_participantMismatch_throws() {
        Festival otherFestival = MockFactory.mockFestival(host, 999L, LocalDate.now(), LocalDate.now().plusDays(1));
        Participant otherParticipant = MockFactory.mockParticipant(targetUser, otherFestival, null, 300L);

        RechargePointRequest request = new RechargePointRequest(otherParticipant.getParticipantId(), 5);

        when(userService.getUserById(1L)).thenReturn(host);
        when(festivalService.getFestivalByIdOrThrow(100L)).thenReturn(festival);
        when(festivalService.isHost(host, festival)).thenReturn(true);
        when(participantService.getParticipantById(300L)).thenReturn(otherParticipant);

        assertThatThrownBy(() ->
                festivalFacade.rechargePoints(1L, 100L, request)
        ).isInstanceOf(FestimateException.class);
    }
}

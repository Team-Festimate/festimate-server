package org.festimate.team.api.facade;

import org.festimate.team.api.point.dto.PointHistoryResponse;
import org.festimate.team.api.admin.dto.RechargePointRequest;
import org.festimate.team.common.mock.MockFactory;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.festimate.team.domain.point.entity.TransactionType;
import org.festimate.team.domain.point.service.PointService;
import org.festimate.team.domain.user.entity.Gender;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.domain.user.service.UserService;
import org.festimate.team.global.exception.FestimateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PointFacadeTest {

    @Mock
    private UserService userService;
    @Mock
    private FestivalService festivalService;
    @Mock
    private ParticipantService participantService;
    @Mock
    private PointService pointService;

    @InjectMocks
    private PointFacade pointFacade;

    private User host;
    private User participantUser;
    private Festival festival;
    private Participant participant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        host = MockFactory.mockUser("호스트", Gender.MAN, 1L);
        participantUser = MockFactory.mockUser("참가자", Gender.WOMAN, 2L);
        festival = MockFactory.mockFestival(host, 100L, LocalDate.now(), LocalDate.now().plusDays(2));
        participant = MockFactory.mockParticipant(participantUser, festival, null, 200L);
    }

    @Test
    @DisplayName("나의 포인트 내역 조회 성공")
    void getMyPointHistory_success() {
        // given
        when(festivalService.getFestivalByIdOrThrow(100L)).thenReturn(festival);
        when(userService.getUserByIdOrThrow(2L)).thenReturn(participantUser);
        when(participantService.getParticipantOrThrow(participantUser, festival)).thenReturn(participant);

        PointHistoryResponse dummyResponse = PointHistoryResponse.from(5, List.of());
        when(pointService.getPointHistory(participant)).thenReturn(dummyResponse);

        // when
        PointHistoryResponse response = pointFacade.getMyPointHistory(2L, 100L);

        // then
        assertThat(response.totalPoint()).isEqualTo(5);
    }

    @Test
    @DisplayName("다른 참가자의 포인트 내역 조회 성공 (호스트 권한)")
    void getParticipantPointHistory_success() {
        // given
        when(userService.getUserByIdOrThrow(1L)).thenReturn(host);
        when(festivalService.getFestivalByIdOrThrow(100L)).thenReturn(festival);
        when(festivalService.isHost(host, festival)).thenReturn(true);
        when(participantService.getParticipantById(200L)).thenReturn(participant);

        PointHistoryResponse dummyResponse = PointHistoryResponse.from(10, List.of());
        when(pointService.getPointHistory(participant)).thenReturn(dummyResponse);

        // when
        PointHistoryResponse response = pointFacade.getParticipantPointHistory(1L, 100L, 200L);

        // then
        assertThat(response.totalPoint()).isEqualTo(10);
    }

    @Test
    @DisplayName("포인트 충전 성공 (호스트 권한)")
    void rechargePoints_success() {
        // given
        RechargePointRequest request = new RechargePointRequest(TransactionType.CREDIT, 200L, 5);

        when(userService.getUserByIdOrThrow(1L)).thenReturn(host);
        when(festivalService.getFestivalByIdOrThrow(100L)).thenReturn(festival);
        when(festivalService.isHost(host, festival)).thenReturn(true);
        when(participantService.getParticipantById(200L)).thenReturn(participant);

        // when
        pointFacade.rechargePoints(1L, 100L, request);

        // then
        verify(pointService).rechargePoint(participant, 5);
    }

    @Test
    @DisplayName("포인트 충전 실패 - 호스트가 아님")
    void rechargePoints_notHost_throws() {
        User attacker = MockFactory.mockUser("악당", Gender.MAN, 999L);
        RechargePointRequest request = new RechargePointRequest(TransactionType.CREDIT, 200L, 5);

        when(userService.getUserByIdOrThrow(999L)).thenReturn(attacker);
        when(festivalService.getFestivalByIdOrThrow(100L)).thenReturn(festival);
        when(festivalService.isHost(attacker, festival)).thenReturn(false);

        // expect
        assertThatThrownBy(() ->
                pointFacade.rechargePoints(999L, 100L, request)
        ).isInstanceOf(FestimateException.class);
    }

    @Test
    @DisplayName("포인트 충전 실패 - 참가자가 해당 축제 참가자 아님")
    void rechargePoints_participantMismatch_throws() {
        Festival otherFestival = MockFactory.mockFestival(host, 999L, LocalDate.now(), LocalDate.now().plusDays(1));
        Participant otherParticipant = MockFactory.mockParticipant(participantUser, otherFestival, null, 300L);

        RechargePointRequest request = new RechargePointRequest(TransactionType.CREDIT, 300L, 5);

        when(userService.getUserByIdOrThrow(1L)).thenReturn(host);
        when(festivalService.getFestivalByIdOrThrow(100L)).thenReturn(festival);
        when(festivalService.isHost(host, festival)).thenReturn(true);
        when(participantService.getParticipantById(300L)).thenReturn(otherParticipant);

        assertThatThrownBy(() ->
                pointFacade.rechargePoints(1L, 100L, request)
        ).isInstanceOf(FestimateException.class);
    }
}

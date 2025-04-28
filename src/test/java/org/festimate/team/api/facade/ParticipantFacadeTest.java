package org.festimate.team.api.facade;

import org.festimate.team.api.festival.dto.MessageRequest;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.domain.matching.service.MatchingService;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.entity.TypeResult;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.festimate.team.domain.point.service.PointService;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.domain.user.service.UserService;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.global.response.ResponseError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ParticipantFacadeTest {

    @Mock
    private UserService userService;
    @Mock
    private FestivalService festivalService;
    @Mock
    private ParticipantService participantService;
    @Mock
    private PointService pointService;
    @Mock
    private MatchingService matchingService;

    @InjectMocks
    private ParticipantFacade participantFacade;

    private User user;
    private Festival festival;
    private Participant participant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder().nickname("테스터").build();
        festival = Festival.builder().host(user).startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1)).build();
        participant = Participant.builder().user(user).festival(festival).typeResult(TypeResult.HEALING).build();
    }

    @Test
    @DisplayName("참가자 입장 성공")
    void entryFestival_success() {
        when(userService.getUserById(1L)).thenReturn(user);
        when(festivalService.getFestivalByIdOrThrow(1L)).thenReturn(festival);
        when(participantService.getParticipant(user, festival)).thenReturn(participant);

        var response = participantFacade.entryFestival(1L, 1L);

        assertThat(response.participantId()).isNotNull();
    }

    @Test
    @DisplayName("참가자 생성 성공")
    void createParticipant_success() {
        when(userService.getUserById(1L)).thenReturn(user);
        when(festivalService.getFestivalByIdOrThrow(1L)).thenReturn(festival);
        when(participantService.createParticipant(any(), any(), any())).thenReturn(participant);

        var response = participantFacade.createParticipant(1L, 1L, null);

        assertThat(response.participantId()).isNotNull();
        verify(matchingService).matchPendingParticipants(participant);
    }

    @Test
    @DisplayName("내 프로필 조회 성공")
    void getParticipantProfile_success() {
        when(userService.getUserById(1L)).thenReturn(user);
        when(festivalService.getFestivalByIdOrThrow(1L)).thenReturn(festival);
        when(participantService.getParticipant(user, festival)).thenReturn(participant);

        var response = participantFacade.getParticipantProfile(1L, 1L);

        assertThat(response.nickname()).isEqualTo("테스터");
    }

    @Test
    @DisplayName("포인트 포함 요약정보 조회 성공")
    void getParticipantSummary_success() {
        when(userService.getUserById(1L)).thenReturn(user);
        when(festivalService.getFestivalByIdOrThrow(1L)).thenReturn(festival);
        when(participantService.getParticipant(user, festival)).thenReturn(participant);
        when(pointService.getTotalPointByParticipant(participant)).thenReturn(10);

        var response = participantFacade.getParticipantSummary(1L, 1L);

        assertThat(response.point()).isEqualTo(10);
    }

    @Test
    @DisplayName("자세한 프로필 조회 성공")
    void getParticipantType_success() {
        when(userService.getUserById(1L)).thenReturn(user);
        when(festivalService.getFestivalByIdOrThrow(1L)).thenReturn(festival);
        when(participantService.getParticipant(user, festival)).thenReturn(participant);

        var response = participantFacade.getParticipantType(1L, 1L);

        assertThat(response.nickname()).isEqualTo("테스터");
    }

    @Test
    @DisplayName("메시지 수정 성공")
    void modifyMessage_success() {
        when(userService.getUserById(1L)).thenReturn(user);
        when(festivalService.getFestivalByIdOrThrow(1L)).thenReturn(festival);
        when(participantService.getParticipant(user, festival)).thenReturn(participant);

        MessageRequest request = new MessageRequest("새로운 소개", "새로운 메세지");

        participantFacade.modifyMessage(1L, 1L, request);

        assertThat(participant.getIntroduction()).isEqualTo("새로운 소개");
        assertThat(participant.getMessage()).isEqualTo("새로운 메세지");
    }

    @Test
    @DisplayName("참가자 조회 실패 시 예외 발생")
    void getParticipant_fail() {
        when(userService.getUserById(1L)).thenReturn(user);
        when(festivalService.getFestivalByIdOrThrow(1L)).thenReturn(festival);
        when(participantService.getParticipant(user, festival)).thenReturn(null);

        assertThatThrownBy(() -> participantFacade.getParticipant(1L, 1L))
                .isInstanceOf(FestimateException.class)
                .hasMessageContaining(ResponseError.PARTICIPANT_NOT_FOUND.getMessage());
    }
}

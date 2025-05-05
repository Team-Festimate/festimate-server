package org.festimate.team.api.facade;

import org.festimate.team.api.festival.dto.FestivalRequest;
import org.festimate.team.api.festival.dto.FestivalVerifyRequest;
import org.festimate.team.common.mock.MockFactory;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.service.FestivalService;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.festimate.team.global.util.DateFormatter.formatPeriod;
import static org.mockito.Mockito.when;

class FestivalFacadeTest {

    @Mock
    private UserService userService;

    @Mock
    private FestivalService festivalService;

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private FestivalFacade festivalFacade;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = MockFactory.mockUser("호스트", Gender.MAN, 1L);
    }

    @Test
    @DisplayName("초대 코드 검증 성공")
    void verifyFestival_success() {
        // given
        FestivalVerifyRequest request = new FestivalVerifyRequest("MOCK123");
        Festival festival = MockFactory.mockFestival(user, 1L, LocalDate.now(), LocalDate.now().plusDays(1));

        when(festivalService.getFestivalByInviteCode("MOCK123")).thenReturn(festival);

        // when
        var response = festivalFacade.verifyFestival(request);

        // then
        assertThat(response.festivalId()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("모의 페스티벌");
    }

    @Test
    @DisplayName("페스티벌 정보 조회 성공")
    void getFestivalInfo_success() {
        // given
        Festival festival = MockFactory.mockFestival(user, 1L, LocalDate.now(), LocalDate.now().plusDays(1));

        when(userService.getUserByIdOrThrow(1L)).thenReturn(user);
        when(festivalService.getFestivalByIdOrThrow(1L)).thenReturn(festival);

        // user가 참가자인지 검증
        // 참가 검증 메서드는 void이므로 별도 when 필요 없음
        // validateParticipation 호출만 정상이라면 통과

        // when
        var response = festivalFacade.getFestivalInfo(1L, 1L);

        // then
        assertThat(response.festivalName()).isEqualTo("모의 페스티벌");
    }

    @Test
    @DisplayName("페스티벌 생성 성공")
    void createFestival_success() {
        // given
        FestivalRequest request = new FestivalRequest(
                "테스트축제",
                "LIFE",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                LocalDateTime.now().plusDays(1)
        );

        Festival festival = MockFactory.mockFestival(user, 100L, request.startDate(), request.endDate());

        when(userService.getUserByIdOrThrow(1L)).thenReturn(user);
        when(festivalService.createFestival(user, request)).thenReturn(festival);

        // when
        var response = festivalFacade.createFestival(1L, request);

        // then
        assertThat(response.festivalId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("내가 참가한 페스티벌 목록 조회 성공")
    void getUserFestivals_success() {
        // given
        Festival festival = MockFactory.mockFestival(user, 200L, LocalDate.now(), LocalDate.now().plusDays(2));

        when(userService.getUserByIdOrThrow(1L)).thenReturn(user);
        when(participantService.getFestivalsByUser(user, "ALL")).thenReturn(List.of(festival));

        // when
        var responses = festivalFacade.getUserFestivals(1L, "ALL");

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).festivalId()).isEqualTo(200L);
    }

    @Test
    @DisplayName("어드민 - 전체 페스티벌 목록 조회 성공")
    void getAllFestivals_success() {
        // given
        Festival festival1 = MockFactory.mockFestival(user, 1L, LocalDate.now(), LocalDate.now().plusDays(1));
        Festival festival2 = MockFactory.mockFestival(user, 2L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(2));

        when(userService.getUserByIdOrThrow(1L)).thenReturn(user);
        when(festivalService.getAllFestivals(user)).thenReturn(List.of(festival1, festival2));

        // when
        var responses = festivalFacade.getAllFestivals(1L);

        // then
        assertThat(responses).hasSize(2);
    }

    @Test
    @DisplayName("어드민 - 특정 페스티벌 상세 조회 성공")
    void getFestivalDetail_success() {
        // given
        Festival festival = MockFactory.mockFestival(user, 1L, LocalDate.now(), LocalDate.now().plusDays(1));

        when(festivalService.getFestivalDetailByIdOrThrow(1L, 1L)).thenReturn(festival);

        // when
        var response = festivalFacade.getFestivalDetail(1L, 1L);

        // then
        assertThat(response.status()).isEqualTo(festival.getFestivalStatus());
        assertThat(response.title()).isEqualTo("모의 페스티벌");
        assertThat(response.festivalDate()).isEqualTo(formatPeriod(festival.getStartDate(), festival.getEndDate()));
        assertThat(response.inviteCode()).isEqualTo("MOCK123");
    }
}

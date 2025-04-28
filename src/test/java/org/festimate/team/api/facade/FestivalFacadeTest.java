package org.festimate.team.api.facade;

import org.festimate.team.api.festival.dto.FestivalRequest;
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

        when(userService.getUserById(1L)).thenReturn(user);
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

        when(userService.getUserById(1L)).thenReturn(user);
        when(participantService.getFestivalsByUser(user, "ALL")).thenReturn(List.of(festival));

        // when
        var responses = festivalFacade.getUserFestivals(1L, "ALL");

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).festivalId()).isEqualTo(200L);
    }
}

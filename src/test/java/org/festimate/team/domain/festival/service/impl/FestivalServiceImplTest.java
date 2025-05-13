package org.festimate.team.domain.festival.service.impl;

import org.festimate.team.common.mock.MockFactory;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.repository.FestivalRepository;
import org.festimate.team.domain.user.entity.Gender;
import org.festimate.team.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

class FestivalServiceImplTest {
    @Mock
    private FestivalRepository festivalRepository;

    @InjectMocks
    private FestivalServiceImpl festivalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("호스트 유저가 개설한 모든 페스티벌 조회")
    void getAllFestivals_success() {
        // given
        User host = MockFactory.mockUser("호스트", Gender.MAN, 1L);
        Festival festival1 = MockFactory.mockFestival(host, 1L, LocalDate.now(), LocalDate.now().plusDays(2));
        Festival festival2 = MockFactory.mockFestival(host, 2L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        List<Festival> expectedFestivals = List.of(festival1, festival2);

        when(festivalRepository.findDistinctByFestivalHosts_Host(host)).thenReturn(expectedFestivals);

        // when
        List<Festival> result = festivalService.getAllFestivals(host);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(festival1, festival2);
    }


    @Test
    @DisplayName("동일한 페스티벌에 여러 호스트가 있을 때, 각 호스트의 조회 결과에 동일한 페스티벌이 반환된다")
    void getAllFestivals_multipleHosts_returnSameFestival() {
        // given
        User host1 = MockFactory.mockUser("1호스트", Gender.MAN, 1L);
        User host2 = MockFactory.mockUser("2호스트", Gender.MAN, 2L);
        Festival festival = MockFactory.mockFestival(host1, 1L, LocalDate.now(), LocalDate.now().plusDays(2));
        festival.addHost(host2);

        List<Festival> expectedFestival1 = List.of(festival);
        List<Festival> expectedFestival2 = List.of(festival);

        when(festivalRepository.findDistinctByFestivalHosts_Host(host1)).thenReturn(expectedFestival1);
        when(festivalRepository.findDistinctByFestivalHosts_Host(host2)).thenReturn(expectedFestival2);

        // when
        List<Festival> result1 = festivalService.getAllFestivals(host1);
        List<Festival> result2 = festivalService.getAllFestivals(host2);

        // then
        assertThat(result1).hasSize(1);
        assertThat(result2).hasSize(1);
        assertThat(result1).containsExactly(festival);
        assertThat(result2).containsExactly(festival);
    }
}

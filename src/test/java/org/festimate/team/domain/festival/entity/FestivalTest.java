package org.festimate.team.domain.festival.entity;

import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.entity.FestivalStatus;
import org.festimate.team.domain.user.entity.Gender;
import org.festimate.team.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.festimate.team.common.mock.MockFactory.mockFestival;
import static org.festimate.team.common.mock.MockFactory.mockUser;

public class FestivalTest {

    private final User mockHost = mockUser("호스트", Gender.MAN, 1L);

    @Test
    @DisplayName("페스티벌 시작 전 상태 확인")
    void testFestivalStatus_before() {
        // given
        LocalDate now = LocalDate.now();
        Festival festival = mockFestival(mockHost, 1L, now.plusDays(1), now.plusDays(2));

        // when
        FestivalStatus status = festival.getFestivalStatus();

        // then
        assertThat(status).isEqualTo(FestivalStatus.BEFORE);
    }

    @Test
    @DisplayName("페스티벌 진행중 상태 확인")
    void testFestivalStatus_progress() {
        // given
        LocalDate now = LocalDate.now();
        Festival festival1 = mockFestival(mockHost, 1L, now.minusDays(1), now);
        Festival festival2 = mockFestival(mockHost, 1L, now, now);
        Festival festival3 = mockFestival(mockHost, 1L, now, now.plusDays(2));
        Festival festival4 = mockFestival(mockHost, 1L, now.minusDays(1), now.plusDays(1));

        // when
        FestivalStatus status1 = festival1.getFestivalStatus();
        FestivalStatus status2 = festival2.getFestivalStatus();
        FestivalStatus status3 = festival3.getFestivalStatus();
        FestivalStatus status4 = festival4.getFestivalStatus();

        // then
        assertThat(status1).isEqualTo(FestivalStatus.PROGRESS);
        assertThat(status2).isEqualTo(FestivalStatus.PROGRESS);
        assertThat(status3).isEqualTo(FestivalStatus.PROGRESS);
        assertThat(status4).isEqualTo(FestivalStatus.PROGRESS);
    }

    @Test
    @DisplayName("페스티벌 환불 기간 상태 확인")
    void testFestivalStatus_refund() {
        // given
        LocalDate now = LocalDate.now();
        Festival festival = mockFestival(mockHost, 1L, now.minusDays(10), now.minusDays(7));

        // when
        FestivalStatus status = festival.getFestivalStatus();

        // then
        assertThat(status).isEqualTo(FestivalStatus.REFUND);
    }

    @Test
    @DisplayName("페스티벌 종료 상태 확인")
    void testFestivalStatus_end() {
        // given
        LocalDate now = LocalDate.now();
        Festival festival = mockFestival(mockHost, 1L, now.minusDays(10), now.minusDays(8));

        // when
        FestivalStatus status = festival.getFestivalStatus();

        // then
        assertThat(status).isEqualTo(FestivalStatus.END);
    }
}

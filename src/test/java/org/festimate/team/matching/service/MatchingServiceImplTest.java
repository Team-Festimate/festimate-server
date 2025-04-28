package org.festimate.team.matching.service;

import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.api.matching.dto.MatchingInfo;
import org.festimate.team.domain.matching.entity.Matching;
import org.festimate.team.domain.matching.entity.MatchingStatus;
import org.festimate.team.domain.matching.repository.MatchingRepository;
import org.festimate.team.domain.matching.service.impl.MatchingServiceImpl;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.entity.TypeResult;
import org.festimate.team.domain.user.entity.Gender;
import org.festimate.team.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.festimate.team.common.mock.MockFactory.*;
import static org.mockito.Mockito.when;

public class MatchingServiceImplTest {
    @Mock
    private MatchingRepository matchingRepository;

    @InjectMocks
    private MatchingServiceImpl matchingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("매칭 리스트 조회 - PENDING과 COMPLETED 매칭을 포함한 정상 케이스")
    void getMatchingListByParticipant_success() {

        // given
        User applicantUser = mockUser("신청자", Gender.MAN, 1L);
        User targetUser = mockUser("타겟", Gender.WOMAN, 2L);

        Participant applicant = Participant.builder()
                .user(applicantUser)
                .build();

        Participant matchedParticipant = Participant.builder()
                .user(targetUser)
                .build();

        Matching completedMatching = Matching.builder()
                .applicantParticipant(applicant)
                .targetParticipant(matchedParticipant)
                .status(MatchingStatus.COMPLETED)
                .matchDate(LocalDateTime.now())
                .build();

        Matching pendingMatching = Matching.builder()
                .applicantParticipant(applicant)
                .targetParticipant(null)
                .status(MatchingStatus.PENDING)
                .matchDate(LocalDateTime.now())
                .build();

        when(matchingRepository.findAllMatchingsByApplicantParticipant(applicant))
                .thenReturn(List.of(completedMatching, pendingMatching));

        // when
        List<MatchingInfo> result = matchingService.getMatchingListByParticipant(applicant);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).nickname()).isEqualTo("타겟");
        assertThat(result.get(1).nickname()).isNull();
    }

    @Test
    @DisplayName("우선순위 기반 매칭 - 매칭 가능한 사람이 있을 때")
    void findBestCandidateByPriority_success() {
        // given
        User applicantUser = mockUser("신청자", Gender.MAN, 1L);
        User targetUser = mockUser("타겟", Gender.WOMAN, 2L);
        Festival festival = mockFestival(applicantUser, 1L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        Participant applicant = Participant.builder()
                .user(applicantUser)
                .typeResult(TypeResult.INFLUENCER)
                .festival(festival)
                .build();
        ReflectionTestUtils.setField(applicant, "participantId", 1L);

        Participant target = Participant.builder()
                .user(targetUser)
                .typeResult(TypeResult.PHOTO)
                .festival(festival)
                .build();

        when(matchingRepository.findMatchingCandidate(
                1L, TypeResult.PHOTO, Gender.MAN, 1L
        )).thenReturn(Optional.of(target));

        Optional<Participant> result = matchingService.findBestCandidateByPriority(
                festival.getFestivalId(), applicant
        );

        assertThat(result).isPresent();
        assertThat(result.get().getTypeResult()).isEqualTo(TypeResult.PHOTO);
    }

    @Test
    @DisplayName("우선순위 기반 매칭 - 1순위 없고 2순위에서 매칭 성공")
    void findBestCandidate_prioritySecond_success() {
        User applicantUser = mockUser("신청자", Gender.MAN, 1L);
        User secondPriorityUser = mockUser("2순위타겟", Gender.WOMAN, 2L);
        Festival festival = mockFestival(applicantUser, 1L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        Participant applicant = Participant.builder()
                .user(applicantUser)
                .typeResult(TypeResult.INFLUENCER)
                .festival(festival)
                .build();
        ReflectionTestUtils.setField(applicant, "participantId", 1L);

        // 1순위 PHOTO에 대상 없음
        when(matchingRepository.findMatchingCandidate(1L, TypeResult.PHOTO, Gender.MAN, 1L))
                .thenReturn(Optional.empty());

        // 2순위 INFLUENCER에 대상 있음
        Participant secondPriorityCandidate = Participant.builder()
                .user(secondPriorityUser)
                .typeResult(TypeResult.INFLUENCER)
                .festival(festival)
                .build();
        when(matchingRepository.findMatchingCandidate(1L, TypeResult.INFLUENCER, Gender.MAN, 1L))
                .thenReturn(Optional.of(secondPriorityCandidate));

        Optional<Participant> result = matchingService.findBestCandidateByPriority(festival.getFestivalId(), applicant);

        assertThat(result).isPresent();
        assertThat(result.get().getTypeResult()).isEqualTo(TypeResult.INFLUENCER);
    }


    @Test
    @DisplayName("우선순위 기반 매칭 - 1,2순위 없고 3순위에서 매칭 성공")
    void findBestCandidate_priorityThird_success() {
        User applicantUser = mockUser("신청자", Gender.MAN, 1L);
        User thirdPriorityUser = mockUser("3순위타겟", Gender.WOMAN, 2L);
        Festival festival = mockFestival(applicantUser, 1L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        Participant applicant = mockParticipant(applicantUser, festival, TypeResult.INFLUENCER, 1L);

        // 1순위 PHOTO, 2순위 INFLUENCER 대상 없음
        when(matchingRepository.findMatchingCandidate(applicant.getParticipantId(), TypeResult.PHOTO, Gender.MAN, festival.getFestivalId()))
                .thenReturn(Optional.empty());
        when(matchingRepository.findMatchingCandidate(applicant.getParticipantId(), TypeResult.INFLUENCER, Gender.MAN, festival.getFestivalId()))
                .thenReturn(Optional.empty());

        // 3순위 NEWBIE에 대상 있음
        Participant thirdPriorityCandidate = mockParticipant(thirdPriorityUser, festival, TypeResult.NEWBIE, 2L);

        when(matchingRepository.findMatchingCandidate(applicant.getParticipantId(), TypeResult.NEWBIE, Gender.MAN, festival.getFestivalId()))
                .thenReturn(Optional.of(thirdPriorityCandidate));

        Optional<Participant> result = matchingService.findBestCandidateByPriority(festival.getFestivalId(), applicant);

        assertThat(result).isPresent();
        assertThat(result.get().getTypeResult()).isEqualTo(TypeResult.NEWBIE);
    }

    @Test
    @DisplayName("우선순위 기반 매칭 - 이미 매칭된 이력 있는 상대만 존재하는 경우 보류 처리")
    void findBestCandidate_alreadyMatchedCandidate_empty() {
        User applicantUser = mockUser("신청자", Gender.MAN, 1L);
        Festival festival = mockFestival(applicantUser, 1L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        Participant applicant = Participant.builder()
                .user(applicantUser)
                .typeResult(TypeResult.INFLUENCER)
                .festival(festival)
                .build();
        ReflectionTestUtils.setField(applicant, "participantId", 1L);

        // 대상이 존재하나 이미 매칭됨 (Repository에서 필터링 됨)
        when(matchingRepository.findMatchingCandidate(1L, TypeResult.PHOTO, Gender.MAN, 1L))
                .thenReturn(Optional.empty());
        when(matchingRepository.findMatchingCandidate(1L, TypeResult.INFLUENCER, Gender.MAN, 1L))
                .thenReturn(Optional.empty());
        when(matchingRepository.findMatchingCandidate(1L, TypeResult.NEWBIE, Gender.MAN, 1L))
                .thenReturn(Optional.empty());

        Optional<Participant> result = matchingService.findBestCandidateByPriority(festival.getFestivalId(), applicant);

        assertThat(result).isEmpty();
    }


    @Test
    @DisplayName("우선순위 기반 매칭 - 매칭 가능한 사람이 없을 때")
    void findBestCandidateByPriority_empty() {
        User user = mockUser("신청자", Gender.MAN, 1L);
        Festival festival = mockFestival(user, 1L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Participant applicant = Participant.builder()
                .user(user)
                .typeResult(TypeResult.INFLUENCER)
                .festival(festival)
                .build();

        when(matchingRepository.findMatchingCandidate(
                applicant.getParticipantId(), TypeResult.PHOTO, Gender.MAN, festival.getFestivalId()
        )).thenReturn(Optional.empty());

        Optional<Participant> result = matchingService.findBestCandidateByPriority(
                festival.getFestivalId(), applicant
        );

        assertThat(result).isEmpty();
    }

}

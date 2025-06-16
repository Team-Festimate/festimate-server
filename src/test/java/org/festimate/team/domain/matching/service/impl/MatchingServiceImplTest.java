package org.festimate.team.domain.matching.service.impl;

import org.festimate.team.api.matching.dto.MatchingListResponse;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.domain.matching.entity.Matching;
import org.festimate.team.domain.matching.entity.MatchingStatus;
import org.festimate.team.domain.matching.repository.MatchingRepository;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.entity.TypeResult;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.festimate.team.domain.point.service.PointService;
import org.festimate.team.domain.user.entity.Gender;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.festimate.team.common.mock.MockFactory.*;
import static org.mockito.Mockito.when;

class MatchingServiceImplTest {
    @Mock
    private MatchingRepository matchingRepository;

    @Mock
    private FestivalService festivalService;

    @Mock
    private ParticipantService participantService;

    @Mock
    private PointService pointService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MatchingServiceImpl matchingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("매칭 리스트 조회 - 정상 케이스")
    void getMatchingList_success() {
        // given
        User user = mockUser("신청자", Gender.MAN, 1L);
        Festival festival = mockFestival(user, 1L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        Participant participant = Participant.builder()
                .user(user)
                .festival(festival)
                .build();

        Matching matching1 = Matching.builder()
                .applicantParticipant(participant)
                .targetParticipant(null)
                .status(MatchingStatus.PENDING)
                .matchDate(LocalDateTime.now())
                .build();

        Matching matching2 = Matching.builder()
                .applicantParticipant(participant)
                .targetParticipant(Participant.builder()
                        .user(mockUser("상대방", Gender.WOMAN, 2L))
                        .build())
                .status(MatchingStatus.COMPLETED)
                .matchDate(LocalDateTime.now())
                .build();

        when(festivalService.getFestivalByIdOrThrow(1L)).thenReturn(festival);
        when(userService.getUserByIdOrThrow(1L)).thenReturn(user);
        when(participantService.getParticipantOrThrow(user, festival)).thenReturn(participant);
        when(matchingRepository.findAllMatchingsByApplicantParticipant(participant))
                .thenReturn(List.of(matching2, matching1));

        // when
        MatchingListResponse result = matchingService.getMatchingList(participant);

        // then
        assertThat(result.matchingList()).hasSize(2);
        assertThat(result.matchingList().get(0).nickname()).isEqualTo("상대방");
        assertThat(result.matchingList().get(1).nickname()).isNull();
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
        List<Long> excludedIds = matchingRepository.findExcludeIds(applicant.getParticipantId());

        Participant target = Participant.builder()
                .user(targetUser)
                .typeResult(TypeResult.PHOTO)
                .festival(festival)
                .build();

        when(matchingRepository.findMatchingCandidatesDsl(1L, TypeResult.PHOTO, Gender.MAN, 1L, PageRequest.of(0, 1), excludedIds))
                .thenReturn(List.of(target));

        var result = matchingService.findBestCandidateByPriority(festival.getFestivalId(), applicant);

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
        List<Long> excludedIds = matchingRepository.findExcludeIds(applicant.getParticipantId());

        // 1순위 PHOTO에 대상 없음
        when(matchingRepository.findMatchingCandidatesDsl(1L, TypeResult.PHOTO, Gender.MAN, 1L, PageRequest.of(0, 1), excludedIds))
                .thenReturn(Collections.emptyList());

        // 2순위 INFLUENCER에 대상 있음
        Participant secondPriorityCandidate = Participant.builder()
                .user(secondPriorityUser)
                .typeResult(TypeResult.INFLUENCER)
                .festival(festival)
                .build();

        when(matchingRepository.findMatchingCandidatesDsl(1L, TypeResult.INFLUENCER, Gender.MAN, 1L, PageRequest.of(0, 1), excludedIds))
                .thenReturn(List.of(secondPriorityCandidate));

        var result = matchingService.findBestCandidateByPriority(festival.getFestivalId(), applicant);

        assertThat(result).isPresent();
        assertThat(result.get().getTypeResult()).isEqualTo(TypeResult.INFLUENCER);
    }


    @Test
    @DisplayName("우선순위 기반 매칭 - 1,2순위 없고 3순위에서 매칭 성공")
    void findBestCandidate_priorityThird_success() {
        User applicantUser = mockUser("신청자", Gender.MAN, 1L);
        User thirdPriorityUser = mockUser("3순위타겟", Gender.WOMAN, 2L);
        Festival festival = mockFestival(applicantUser, 1L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        // 신청자
        Participant applicant = mockParticipant(applicantUser, festival, TypeResult.INFLUENCER, 1L);
        List<Long> excludedIds = matchingRepository.findExcludeIds(applicant.getParticipantId());

        // 3순위 NEWBIE에 대상 있음
        Participant thirdPriorityCandidate = mockParticipant(thirdPriorityUser, festival, TypeResult.NEWBIE, 2L);

        // 1순위 PHOTO, 2순위 INFLUENCER 대상 없음
        when(matchingRepository.findMatchingCandidatesDsl(1L, TypeResult.PHOTO, Gender.MAN, 1L, PageRequest.of(0, 1), excludedIds))
                .thenReturn(Collections.emptyList());
        when(matchingRepository.findMatchingCandidatesDsl(1L, TypeResult.INFLUENCER, Gender.MAN, 1L, PageRequest.of(0, 1), excludedIds))
                .thenReturn(Collections.emptyList());
        // 3순위 대상 있음
        when(matchingRepository.findMatchingCandidatesDsl(1L, TypeResult.NEWBIE, Gender.MAN, 1L, PageRequest.of(0, 1), excludedIds))
                .thenReturn(List.of(thirdPriorityCandidate));

        var result = matchingService.findBestCandidateByPriority(festival.getFestivalId(), applicant);

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
        List<Long> excludedIds = matchingRepository.findExcludeIds(applicant.getParticipantId());

        // 대상이 존재하나 이미 매칭됨 (Repository에서 필터링 됨)
        when(matchingRepository.findMatchingCandidatesDsl(1L, TypeResult.PHOTO, Gender.MAN, 1L, PageRequest.of(0, 1), excludedIds))
                .thenReturn(Collections.emptyList());
        when(matchingRepository.findMatchingCandidatesDsl(1L, TypeResult.INFLUENCER, Gender.MAN, 1L, PageRequest.of(0, 1), excludedIds))
                .thenReturn(Collections.emptyList());
        when(matchingRepository.findMatchingCandidatesDsl(1L, TypeResult.NEWBIE, Gender.MAN, 1L, PageRequest.of(0, 1), excludedIds))
                .thenReturn(Collections.emptyList());

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
        List<Long> excludedIds = matchingRepository.findExcludeIds(applicant.getParticipantId());

        when(matchingRepository.findMatchingCandidatesDsl(
                applicant.getParticipantId(), TypeResult.PHOTO, Gender.MAN, festival.getFestivalId(), PageRequest.of(0, 1), excludedIds
        )).thenReturn(Collections.emptyList());

        Optional<Participant> result = matchingService.findBestCandidateByPriority(
                festival.getFestivalId(), applicant
        );

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("매칭 상세 조회 - 매칭과 페스티벌이 일치하지 않는 경우 예외 발생")
    void getMatchingDetail_invalidFestival_throwsException() {
        // given
        User user = mockUser("사용자", Gender.MAN, 1L);
        Festival requestedFestival = mockFestival(user, 1L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Festival otherFestival = mockFestival(user, 2L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        Participant participant = mockParticipant(user, requestedFestival, TypeResult.INFLUENCER, 1L);
        Matching mismatchedMatching = Matching.builder()
                .festival(otherFestival)
                .applicantParticipant(participant)
                .targetParticipant(Participant.builder()
                        .user(mockUser("상대방", Gender.WOMAN, 3L))
                        .build())
                .status(MatchingStatus.COMPLETED)
                .matchDate(LocalDateTime.now())
                .build();

        when(userService.getUserByIdOrThrow(user.getUserId())).thenReturn(user);
        when(festivalService.getFestivalByIdOrThrow(requestedFestival.getFestivalId())).thenReturn(requestedFestival);
        when(participantService.getParticipantOrThrow(user, requestedFestival)).thenReturn(participant);
        when(matchingRepository.findByMatchingId(1L)).thenReturn(Optional.of(mismatchedMatching));

        // when & then
        assertThatThrownBy(() -> matchingService.getMatchingDetail(participant, requestedFestival, 1L))
                .isInstanceOf(FestimateException.class)
                .hasMessage(ResponseError.FORBIDDEN_RESOURCE.getMessage());
    }

    @Test
    @DisplayName("매칭 상세 조회 - 다른 사람이 신청한 매칭을 조회할 경우 예외 발생")
    void getMatchingDetail_invalidApplicant_throwsException() {
        // given
        User user = mockUser("사용자1", Gender.MAN, 1L);
        User otherUser = mockUser("사용자2", Gender.MAN, 2L);
        Festival festival = mockFestival(user, 1L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        Participant myParticipant = mockParticipant(user, festival, TypeResult.INFLUENCER, 1L);
        Participant otherParticipant = mockParticipant(otherUser, festival, TypeResult.INFLUENCER, 2L);

        Matching otherMatching = Matching.builder()
                .festival(festival)
                .applicantParticipant(otherParticipant)
                .targetParticipant(Participant.builder()
                        .user(mockUser("상대방", Gender.WOMAN, 3L))
                        .build())
                .status(MatchingStatus.COMPLETED)
                .matchDate(LocalDateTime.now())
                .build();

        when(userService.getUserByIdOrThrow(user.getUserId())).thenReturn(user);
        when(festivalService.getFestivalByIdOrThrow(festival.getFestivalId())).thenReturn(festival);
        when(participantService.getParticipantOrThrow(user, festival)).thenReturn(myParticipant);
        when(matchingRepository.findByMatchingId(1L)).thenReturn(Optional.of(otherMatching));

        // when & then
        assertThatThrownBy(() -> matchingService.getMatchingDetail(myParticipant, festival, 1L))
                .isInstanceOf(FestimateException.class)
                .hasMessage(ResponseError.FORBIDDEN_RESOURCE.getMessage());
    }

    @Test
    @DisplayName("매칭 상세 조회 - 보류 중인 매칭을 조회할 경우 예외 발생")
    void getMatchingDetail_pendingMatching_throwsException() {
        // given
        User user = mockUser("사용자", Gender.MAN, 1L);
        Festival requestedFestival = mockFestival(user, 1L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Festival otherFestival = mockFestival(user, 2L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        Participant participant = mockParticipant(user, requestedFestival, TypeResult.INFLUENCER, 1L);
        Matching mismatchedMatching = Matching.builder()
                .festival(otherFestival)
                .applicantParticipant(participant)
                .targetParticipant(null)
                .status(MatchingStatus.COMPLETED)
                .matchDate(LocalDateTime.now())
                .build();

        when(userService.getUserByIdOrThrow(user.getUserId())).thenReturn(user);
        when(festivalService.getFestivalByIdOrThrow(requestedFestival.getFestivalId())).thenReturn(requestedFestival);
        when(participantService.getParticipantOrThrow(user, requestedFestival)).thenReturn(participant);
        when(matchingRepository.findByMatchingId(1L)).thenReturn(Optional.of(mismatchedMatching));

        // when & then
        assertThatThrownBy(() -> matchingService.getMatchingDetail(participant, requestedFestival, 1L))
                .isInstanceOf(FestimateException.class)
                .hasMessage(ResponseError.TARGET_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("매칭 상세 조회 - 없는 매칭 ID를 조회할 경우 예외 발생")
    void getMatchingDetail_nonExistentMatchingId_throwsException() {
        // given
        User user = mockUser("사용자", Gender.MAN, 1L);
        Festival festival = mockFestival(user, 1L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Participant participant = mockParticipant(user, festival, TypeResult.INFLUENCER, 1L);
        Matching mismatchedMatching = Matching.builder()
                .festival(festival)
                .applicantParticipant(participant)
                .targetParticipant(Participant.builder()
                        .user(mockUser("상대방", Gender.WOMAN, 3L))
                        .build())
                .status(MatchingStatus.COMPLETED)
                .matchDate(LocalDateTime.now())
                .build();

        when(userService.getUserByIdOrThrow(user.getUserId())).thenReturn(user);
        when(festivalService.getFestivalByIdOrThrow(festival.getFestivalId())).thenReturn(festival);
        when(participantService.getParticipantOrThrow(user, festival)).thenReturn(participant);
        when(matchingRepository.findByMatchingId(1L))
                .thenReturn(Optional.ofNullable(mismatchedMatching));

        // when & then
        assertThatThrownBy(() -> matchingService.getMatchingDetail(participant, festival, 2L))
                .isInstanceOf(FestimateException.class)
                .hasMessage(ResponseError.TARGET_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("우선순위 기반 매칭 - 후보가 2명 이상일 때 첫 번째만 선택")
    void findBestCandidate_multipleCandidates_returnsFirstOnly() {
        // given
        User applicantUser = mockUser("신청자", Gender.MAN, 1L);
        Festival festival = mockFestival(applicantUser, 1L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Participant applicant = mockParticipant(applicantUser, festival, TypeResult.INFLUENCER, 1L);
        List<Long> excludedIds = matchingRepository.findExcludeIds(applicant.getParticipantId());

        User candidate1 = mockUser("후보1", Gender.WOMAN, 2L);
        User candidate2 = mockUser("후보2", Gender.WOMAN, 3L);
        Participant p1 = mockParticipant(candidate1, festival, TypeResult.PHOTO, 2L);
        Participant p2 = mockParticipant(candidate2, festival, TypeResult.PHOTO, 3L);

        // 후보 2명 반환
        when(matchingRepository.findMatchingCandidatesDsl(1L, TypeResult.PHOTO, Gender.MAN, 1L, PageRequest.of(0, 1), excludedIds))
                .thenReturn(List.of(p1, p2));

        // when
        var result = matchingService.findBestCandidateByPriority(festival.getFestivalId(), applicant);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUser().getNickname()).isEqualTo("후보1");
    }


}

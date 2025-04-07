package org.festimate.team.matching.service;

import org.festimate.team.festival.entity.Category;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.matching.dto.MatchingInfo;
import org.festimate.team.matching.entity.Matching;
import org.festimate.team.matching.entity.MatchingStatus;
import org.festimate.team.matching.repository.MatchingRepository;
import org.festimate.team.matching.service.impl.MatchingServiceImpl;
import org.festimate.team.participant.entity.Participant;
import org.festimate.team.participant.entity.TypeResult;
import org.festimate.team.user.entity.*;
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
        User applicationUser = mockUser("신청자", Gender.MAN);
        User targetUser = mockUser("타겟", Gender.WOMAN);

        Participant applicant = Participant.builder()
                .user(applicationUser)
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
        User applicationUser = mockUser("신청자", Gender.MAN);
        User targetUser = mockUser("타겟", Gender.WOMAN);

        Festival festival = mockFestival(applicationUser);

        Participant applicant = Participant.builder()
                .user(applicationUser)
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
    @DisplayName("우선순위 기반 매칭 - 매칭 가능한 사람이 없을 때")
    void findBestCandidateByPriority_empty() {
        User user = mockUser("신청자", Gender.MAN);
        Festival festival = mockFestival(user);
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

    private User mockUser(String nickname, Gender gender) {
        return User.builder()
                .name("남자")
                .phoneNumber("010-1234-5678")
                .nickname(nickname)
                .birthYear(1999)
                .gender(gender)
                .mbti(Mbti.INFP)
                .appearanceType(AppearanceType.BEAR)
                .platformId("1")
                .platform(Platform.KAKAO)
                .refreshToken("dummy_refresh_token")
                .build();
    }

    private Festival mockFestival(User mockUser) {
        Festival festival = Festival.builder()
                .host(mockUser)
                .title("가톨릭대학교 다맛제")
                .category(Category.LIFE)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(2))
                .inviteCode("ABC123")
                .build();

        ReflectionTestUtils.setField(festival, "festivalId", 1L);
        return festival;
    }

}

package org.festimate.team.facade;

import org.festimate.team.api.facade.FestivalFacade;
import org.festimate.team.global.response.ResponseError;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.api.festival.dto.FestivalInfoResponse;
import org.festimate.team.api.festival.dto.MessageRequest;
import org.festimate.team.domain.festival.entity.Category;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.entity.TypeResult;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.festimate.team.domain.user.entity.AppearanceType;
import org.festimate.team.domain.user.entity.Mbti;
import org.festimate.team.domain.user.entity.Platform;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("페스티벌 상세 조회 - 성공")
    void getFestivalInfo_success() {
        // given
        Long userId = 1L;
        Long festivalId = 100L;
        User mockUser = User.builder()
                .name("테스트 유저")
                .phoneNumber("010-1234-5678")
                .nickname("현진")
                .birthYear(1999)
                .mbti(Mbti.INFP)
                .appearanceType(AppearanceType.BEAR)
                .platformId("kakao_123456")
                .platform(Platform.KAKAO)
                .refreshToken("dummy_refresh_token")
                .build();


        Festival mockFestival = Festival.builder()
                .host(mockUser)
                .title("가톨릭대학교 다맛제")
                .category(Category.LIFE)
                .startDate(LocalDate.of(2025, 5, 18))
                .endDate(LocalDate.of(2025, 5, 20))
                .inviteCode("ABC123")
                .build();

        Participant mockParticipant = Participant.builder()
                .user(mockUser)
                .festival(mockFestival)
                .typeResult(TypeResult.HEALING)
                .introduction("안녕하세요~ 제 전화번호는요~")
                .message("잘 부탁드려요 :)")
                .build();

        when(userService.getUserById(userId)).thenReturn(mockUser);
        when(festivalService.getFestivalByIdOrThrow(festivalId)).thenReturn(mockFestival);
        when(participantService.getParticipant(mockUser, mockFestival)).thenReturn(mockParticipant);

        // when
        festivalFacade.validateUserParticipation(userId, mockFestival);
        FestivalInfoResponse response = FestivalInfoResponse.of(mockFestival);

        // then
        assertThat(response.festivalName()).isEqualTo("가톨릭대학교 다맛제");
        assertThat(response.festivalDate()).isEqualTo("2025.05.18 ~ 2025.05.20");
    }

    @Test
    @DisplayName("페스티벌 상세 조회 - 유저가 참가자가 아닌 경우 예외 발생")
    void getFestivalInfo_fail_if_not_participant() {
        // given
        Long userId = 2L;

        User mockUser = User.builder()
                .name("테스트 유저")
                .phoneNumber("010-1234-5678")
                .nickname("현진")
                .birthYear(1999)
                .mbti(Mbti.INFP)
                .appearanceType(AppearanceType.BEAR)
                .platformId("kakao_123456")
                .platform(Platform.KAKAO)
                .refreshToken("dummy_refresh_token")
                .build();

        Festival mockFestival = Festival.builder()
                .host(mockUser)
                .title("가톨릭대학교 다맛제")
                .category(Category.LIFE)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(2))
                .inviteCode("ABC123")
                .build();

        when(userService.getUserById(userId)).thenReturn(mockUser);
        when(participantService.getParticipant(mockUser, mockFestival)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> festivalFacade.validateUserParticipation(userId, mockFestival))
                .isInstanceOf(FestimateException.class)
                .hasMessageContaining(ResponseError.FORBIDDEN_RESOURCE.getMessage());
    }

    @Test
    @DisplayName("자기소개 및 메시지 수정 - 정상 케이스 (message 생략 가능)")
    void modifyMessage_success() {
        // given
        Long userId = 1L;
        Festival mockFestival = mockFestival();
        User mockUser = mockUser();
        Participant mockParticipant = Participant.builder()
                .user(mockUser)
                .festival(mockFestival)
                .typeResult(TypeResult.HEALING)
                .introduction("이전 자기소개")
                .message("이전 메세지")
                .build();

        MessageRequest request = new MessageRequest("메세지 수정했지롱 ~", null);

        when(userService.getUserById(userId)).thenReturn(mockUser);
        when(participantService.getParticipant(mockUser, mockFestival)).thenReturn(mockParticipant);

        // when
        festivalFacade.modifyMyMessage(userId, mockFestival, request);

        // then
        assertThat(mockParticipant.getIntroduction()).isEqualTo("메세지 수정했지롱 ~");
        assertThat(mockParticipant.getMessage()).isNull();
    }

    @Test
    @DisplayName("자기소개 및 메시지 수정 - message 생략, introduction null일 경우 예외 발생")
    void modifyMessage_fail_if_introduction_missing() {
        // given
        Long userId = 1L;
        Festival mockFestival = mockFestival();
        User mockUser = mockUser();
        Participant mockParticipant = Participant.builder()
                .user(mockUser)
                .festival(mockFestival)
                .typeResult(TypeResult.HEALING)
                .introduction("이전 자기소개")
                .message("이전 메세지")
                .build();

        MessageRequest request = new MessageRequest(null, "롱롱소세지");

        when(userService.getUserById(userId)).thenReturn(mockUser);
        when(participantService.getParticipant(mockUser, mockFestival)).thenReturn(mockParticipant);

        // when & then
        assertThatThrownBy(() -> festivalFacade.modifyMyMessage(userId, mockFestival, request))
                .isInstanceOf(FestimateException.class)
                .hasMessageContaining(ResponseError.BAD_REQUEST.getMessage());
    }

    @Test
    @DisplayName("자기소개 및 메시지 수정 - 유저가 페스티벌 참가자가 아닌 경우")
    void modifyMessage_fail_if_not_participant() {
        // given
        Long userId = 2L;
        Festival mockFestival = mockFestival();
        User mockUser = mockUser();

        MessageRequest request = new MessageRequest("메세지 수정했지롱 ~", "롱롱소세지");

        when(userService.getUserById(userId)).thenReturn(mockUser);
        when(participantService.getParticipant(mockUser, mockFestival)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> festivalFacade.modifyMyMessage(userId, mockFestival, request))
                .isInstanceOf(FestimateException.class)
                .hasMessageContaining(ResponseError.FORBIDDEN_RESOURCE.getMessage());
    }

    @Test
    @DisplayName("자기소개 및 메시지 수정 - 모든 항목 올바르게 입력한 경우")
    void modifyMessage_success_if_all_fields_given() {
        // given
        Long userId = 1L;
        Festival mockFestival = mockFestival();
        User mockUser = mockUser();
        Participant mockParticipant = Participant.builder()
                .user(mockUser)
                .festival(mockFestival)
                .typeResult(TypeResult.HEALING)
                .introduction("이전 자기소개")
                .message("이전 메세지")
                .build();

        MessageRequest request = new MessageRequest("새로운 소개", "새로운 메시지");

        when(userService.getUserById(userId)).thenReturn(mockUser);
        when(participantService.getParticipant(mockUser, mockFestival)).thenReturn(mockParticipant);

        // when
        festivalFacade.modifyMyMessage(userId, mockFestival, request);

        // then
        assertThat(mockParticipant.getIntroduction()).isEqualTo("새로운 소개");
        assertThat(mockParticipant.getMessage()).isEqualTo("새로운 메시지");
    }

    private User mockUser() {
        return User.builder()
                .name("테스트 유저")
                .phoneNumber("010-1234-5678")
                .nickname("현진")
                .birthYear(1999)
                .mbti(Mbti.INFP)
                .appearanceType(AppearanceType.BEAR)
                .platformId("kakao_123456")
                .platform(Platform.KAKAO)
                .refreshToken("dummy_refresh_token")
                .build();
    }

    private Festival mockFestival() {
        return Festival.builder()
                .host(mockUser())
                .title("가톨릭대학교 다맛제")
                .category(Category.LIFE)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(2))
                .inviteCode("ABC123")
                .build();
    }

}

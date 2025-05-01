package org.festimate.team.common.mock;

import org.festimate.team.domain.festival.entity.Category;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.entity.TypeResult;
import org.festimate.team.domain.user.entity.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

public class MockFactory {

    public static User mockUser(String nickname, Gender gender, long id) {
        User user = User.builder()
                .name("테스트유저")
                .phoneNumber("010-1234-5678")
                .nickname(nickname)
                .birthYear(1999)
                .gender(gender)
                .mbti(Mbti.INFP)
                .appearanceType(AppearanceType.BEAR)
                .platformId("123456")
                .platform(Platform.KAKAO)
                .refreshToken("mock_refresh_token")
                .build();
        ReflectionTestUtils.setField(user, "userId", id);
        return user;
    }

    public static Festival mockFestival(User host, long id, LocalDate startDate, LocalDate endDate) {
        Festival festival = Festival.builder()
                .host(host)
                .title("모의 페스티벌")
                .category(Category.LIFE)
                .startDate(startDate)
                .endDate(endDate)
                .matchingStartAt(startDate.atStartOfDay().plusHours(1))
                .inviteCode("MOCK123")
                .build();

        ReflectionTestUtils.setField(festival, "festivalId", id);
        return festival;
    }

    public static Participant mockParticipant(User user, Festival festival, TypeResult typeResult, long id) {
        Participant participant = Participant.builder()
                .user(user)
                .festival(festival)
                .typeResult(typeResult)
                .build();
        ReflectionTestUtils.setField(participant, "participantId", id);
        return participant;
    }
}

package org.festimate.team.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.festimate.team.common.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicUpdate
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private int birthYear;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    @Column(nullable = false)
    private AppearanceType appearanceType;

    @Column(nullable = false, unique = true)
    private String platformId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Lob
    private String refreshToken;

    @Builder
    public User(String name, String phoneNumber, String nickname, Integer birthYear, Mbti mbti,
                AppearanceType appearanceType, String platformId, Platform platform, String refreshToken) {
        this.name = (name != null) ? name : "페스티메이트";
        this.phoneNumber = (phoneNumber != null) ? phoneNumber : "000-0000-0000";
        this.nickname = (nickname != null) ? nickname : "메이트";
        this.birthYear = (birthYear != null) ? birthYear : 2000;
        this.mbti = (mbti != null) ? mbti : Mbti.ENFJ;
        this.appearanceType = (appearanceType != null) ? appearanceType : AppearanceType.DOG;
        this.platformId = (platformId != null) ? platformId : "KAKAO";
        this.platform = (platform != null) ? platform : Platform.KAKAO;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
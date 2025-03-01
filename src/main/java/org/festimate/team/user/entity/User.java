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
    private int appearanceType;

    @Column(nullable = false)
    private String platformId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Lob
    private String refreshToken;

    @Builder
    public User(String name, String phoneNumber, String nickname, Integer birthYear, Mbti mbti,
                Integer appearanceType, String platformId, Platform platform, String refreshToken) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.birthYear = birthYear;
        this.mbti = mbti;
        this.appearanceType = appearanceType;
        this.platformId = platformId;
        this.platform = platform;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
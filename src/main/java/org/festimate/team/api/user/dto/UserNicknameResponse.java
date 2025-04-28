package org.festimate.team.api.user.dto;

public record UserNicknameResponse(
        String nickname
) {
    public static UserNicknameResponse from(String nickname) {
        return new UserNicknameResponse(nickname);
    }
}

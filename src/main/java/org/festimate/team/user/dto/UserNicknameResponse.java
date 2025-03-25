package org.festimate.team.user.dto;

public record UserNicknameResponse(
        String nickanme
) {
    public static UserNicknameResponse from(String nickanme) {
        return new UserNicknameResponse(nickanme);
    }
}

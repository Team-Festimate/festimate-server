package org.festimate.team.common.validator;

public class LengthValidator {
    public static boolean rangeLengthCheck(String text, int minLength, int maxLength) {
        return text.length() >= minLength && text.length() <= maxLength;
    }
}

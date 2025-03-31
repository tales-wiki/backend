package com.openmpy.taleswiki.member.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_MEMBER_EMAIL;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_MEMBER_EMAIL_NULL_AND_BLANK;

import com.openmpy.taleswiki.common.exception.CustomException;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MemberEmail {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private String value;

    public MemberEmail(final String value) {
        validateBlank(value);
        validateEmail(value);

        this.value = value;
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException(NOT_ALLOWED_MEMBER_EMAIL_NULL_AND_BLANK);
        }
    }

    private void validateEmail(final String value) {
        if (!isValidEmail(value)) {
            throw new CustomException(INVALID_MEMBER_EMAIL, value);
        }
    }

    private boolean isValidEmail(final String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }
}

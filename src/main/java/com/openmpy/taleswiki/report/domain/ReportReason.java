package com.openmpy.taleswiki.report.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_REPORT_REASON_LENGTH;

import com.openmpy.taleswiki.common.exception.CustomException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ReportReason {

    private static final int REPORT_REASON_MAX_LENGTH = 100;

    private String value;

    public ReportReason(final String value) {
        validateLength(value);

        this.value = value;
    }

    private void validateLength(final String value) {
        if (value.length() > REPORT_REASON_MAX_LENGTH) {
            throw new CustomException(INVALID_REPORT_REASON_LENGTH);
        }
    }
}

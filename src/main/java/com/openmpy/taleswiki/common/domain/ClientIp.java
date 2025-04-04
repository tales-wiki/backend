package com.openmpy.taleswiki.common.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_IP;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_IP_NULL_AND_BLANK;

import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.common.util.IpAddressUtil;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ClientIp {

    private String value;

    public ClientIp(final String value) {
        validateBlank(value);
        validateIp(value);

        this.value = value;
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException(NOT_ALLOWED_IP_NULL_AND_BLANK);
        }
    }

    private void validateIp(final String value) {
        if (!IpAddressUtil.isValidIPv4(value) && !IpAddressUtil.isValidIPv6(value)) {
            throw new CustomException(INVALID_IP);
        }
    }
}

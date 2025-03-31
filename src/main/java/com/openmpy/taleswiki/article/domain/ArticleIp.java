package com.openmpy.taleswiki.article.domain;

import com.openmpy.taleswiki.common.util.IpAddressUtil;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ArticleIp {

    private String value;

    public ArticleIp(final String value) {
        validateBlank(value);
        validateIp(value);

        this.value = value;
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Ip가 빈 값일 수 없습니다.");
        }
    }

    private void validateIp(final String value) {
        if (!IpAddressUtil.isValidIPv4(value) && !IpAddressUtil.isValidIPv6(value)) {
            throw new IllegalArgumentException("올바르지 않은 Ip 입니다.");
        }
    }
}

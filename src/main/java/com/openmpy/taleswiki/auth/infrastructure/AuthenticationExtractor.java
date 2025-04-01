package com.openmpy.taleswiki.auth.infrastructure;

import com.openmpy.taleswiki.common.exception.AuthenticationException;
import com.openmpy.taleswiki.common.exception.CustomErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class AuthenticationExtractor {

    public String extract(final HttpServletRequest servletRequest, final String cookieName) {
        final Cookie[] cookies = servletRequest.getCookies();
        if (cookies == null) {
            throw new AuthenticationException(CustomErrorCode.NOT_FOUND_COOKIE, cookieName);
        }

        return Arrays.stream(cookies)
                .filter(it -> cookieName.equals(it.getName()))
                .findFirst()
                .orElseThrow(() -> new AuthenticationException(CustomErrorCode.NOT_FOUND_COOKIE, cookieName))
                .getValue();
    }
}

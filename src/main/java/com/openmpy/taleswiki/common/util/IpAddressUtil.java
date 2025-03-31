package com.openmpy.taleswiki.common.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

public class IpAddressUtil {

    private static final String IP_V4_PATTERN = "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$";
    private static final String IP_V6_PATTERN = "^(([0-9a-fA-F]{1,4}):){7}([0-9a-fA-F]{1,4})$";

    private IpAddressUtil() {
        throw new IllegalStateException("유틸리티 클래스입니다.");
    }

    public static String getClientIp(final HttpServletRequest servletRequest) {
        final String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR",
        };

        for (final String header : headers) {
            final String ip = servletRequest.getHeader(header);

            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return servletRequest.getRemoteAddr();
    }

    public static boolean isValidIPv4(final String ip) {
        return Pattern.matches(IP_V4_PATTERN, ip);
    }

    public static boolean isValidIPv6(final String ip) {
        return Pattern.matches(IP_V6_PATTERN, ip);
    }
}

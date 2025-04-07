package com.openmpy.taleswiki.common.exception;

import com.openmpy.taleswiki.common.util.IpAddressUtil;
import com.openmpy.taleswiki.discord.application.DiscordService;
import com.openmpy.taleswiki.discord.application.request.DiscordExceptionRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final DiscordService discordService;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customException(
            final CustomException e,
            final HttpServletRequest servletRequest
    ) {
        final DiscordExceptionRequest request = new DiscordExceptionRequest(
                e.getErrorCode().name(),
                e.getMessage(),
                servletRequest.getMethod() + " " + servletRequest.getRequestURI(),
                getRequestPayload(servletRequest),
                IpAddressUtil.getClientIp(servletRequest)
        );

        discordService.sendWaringMessage(request);
        log.warn(e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationException(
            final AuthenticationException e,
            final HttpServletRequest servletRequest
    ) {
        final DiscordExceptionRequest request = new DiscordExceptionRequest(
                e.getErrorCode().name(),
                e.getMessage(),
                servletRequest.getMethod() + " " + servletRequest.getRequestURI(),
                getRequestPayloadWithLogin(servletRequest),
                IpAddressUtil.getClientIp(servletRequest)
        );

        discordService.sendWaringMessage(request);
        log.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> httpRequestMethodNotSupportedException(
            final HttpRequestMethodNotSupportedException e
    ) {
        log.warn(e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(CustomErrorCode.REQUEST_METHOD_NOT_SUPPORTED));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> noResourceException(final NoResourceFoundException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(CustomErrorCode.NO_RESOURCE_REQUEST));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException(final HttpMessageNotReadableException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(CustomErrorCode.MESSAGE_NOT_READABLE));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(
            final Exception e,
            final HttpServletRequest servletRequest
    ) {
        final DiscordExceptionRequest request = new DiscordExceptionRequest(
                "NONE",
                e.getMessage(),
                servletRequest.getMethod() + " " + servletRequest.getRequestURI(),
                getRequestPayload(servletRequest),
                IpAddressUtil.getClientIp(servletRequest)
        );

        discordService.sendErrorMessage(request);
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body(ErrorResponse.of(CustomErrorCode.INTERNAL_SERVER_ERROR));
    }

    private String getRequestPayloadWithLogin(final HttpServletRequest servletRequest) {
        try (BufferedReader reader = servletRequest.getReader()) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator() + "\t"));
        } catch (final IOException e) {
            log.error("Request Payload 값을 읽지 못했습니다.", e);
            return "에러";
        }
    }

    private String getRequestPayload(final HttpServletRequest servletRequest) {
        if (servletRequest instanceof final ContentCachingRequestWrapper wrapperRequest) {
            final byte[] bytes = wrapperRequest.getContentAsByteArray();
            if (bytes.length > 0) {
                try {
                    return new String(bytes, wrapperRequest.getCharacterEncoding());
                } catch (final UnsupportedEncodingException e) {
                    log.error("Request Payload 값을 읽지 못했습니다.", e);
                    return "에러";
                }
            }
        }
        return "";
    }
}

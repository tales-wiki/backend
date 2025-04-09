package com.openmpy.taleswiki.common.infrastructure;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.BLOCKED_IP;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.openmpy.taleswiki.admin.domain.repository.BlockedIpRepository;
import com.openmpy.taleswiki.common.exception.AuthenticationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class RequestServletFilterTest {

    @Autowired
    private RequestServletFilter servletFilter;

    @MockitoBean
    private BlockedIpRepository blockedIpRepository;

    @DisplayName("[통과] 정지된 아이피가 아닐 경우 요청을 허용한다.")
    @Test
    void request_servlet_filter_test_01() throws Exception {
        // given
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRemoteAddr("127.0.0.1");

        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final MockFilterChain filterChain = new MockFilterChain();

        // stub
        when(blockedIpRepository.existsByIp_Value("127.0.0.1")).thenReturn(false);

        // when
        servletFilter.doFilter(servletRequest, servletResponse, filterChain);

        // then
        verify(blockedIpRepository, times(1)).existsByIp_Value("127.0.0.1");
    }

    @DisplayName("[예외] 정지된 아이피일 경우 요청을 금지한다.")
    @Test
    void 예외_request_servlet_filter_test_01() {
        // given
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRemoteAddr("127.0.0.1");

        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final MockFilterChain filterChain = new MockFilterChain();

        // stub
        when(blockedIpRepository.existsByIp_Value("127.0.0.1")).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> servletFilter.doFilter(servletRequest, servletResponse, filterChain))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage(BLOCKED_IP.getMessage());
    }
}
package com.openmpy.taleswiki.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BlockedIpTest {

    @DisplayName("[통과] 아이피 블록 객체 생성자를 검사한다.")
    @Test
    void blocked_ip_test_01() {
        // given
        final long id = 1L;
        final String ip = "127.0.0.1";

        // when
        final BlockedIp blockedIp = new BlockedIp(id, ip);

        // then
        assertThat(blockedIp.getId()).isEqualTo(1L);
        assertThat(blockedIp.getIp()).isEqualTo("127.0.0.1");
    }

    @DisplayName("[통과] 아이피 블록 객체를 생성한다.")
    @Test
    void blocked_ip_test_02() {
        // given
        final String ip = "127.0.0.1";

        // when
        final BlockedIp blockedIp = BlockedIp.create(ip);

        // then
        assertThat(blockedIp.getIp()).isEqualTo("127.0.0.1");
    }
}
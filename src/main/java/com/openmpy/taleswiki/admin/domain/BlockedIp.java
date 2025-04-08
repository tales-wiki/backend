package com.openmpy.taleswiki.admin.domain;

import com.openmpy.taleswiki.common.domain.BaseEntity;
import com.openmpy.taleswiki.common.domain.ClientIp;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class BlockedIp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "client_ip", nullable = false))
    private ClientIp ip;

    public BlockedIp(final Long id, final String ip) {
        this.id = id;
        this.ip = new ClientIp(ip);
    }

    public BlockedIp(final String ip) {
        this.ip = new ClientIp(ip);
    }

    public static BlockedIp create(final String ip) {
        return new BlockedIp(ip);
    }

    public String getIp() {
        return ip.getValue();
    }
}

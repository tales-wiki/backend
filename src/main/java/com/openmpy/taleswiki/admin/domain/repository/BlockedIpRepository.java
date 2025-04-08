package com.openmpy.taleswiki.admin.domain.repository;

import com.openmpy.taleswiki.admin.domain.BlockedIp;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedIpRepository extends JpaRepository<BlockedIp, Long> {

    Optional<BlockedIp> findByIp_Value(final String ip);

    boolean existsByIp_Value(final String ip);
}

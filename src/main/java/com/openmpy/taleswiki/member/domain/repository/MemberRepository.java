package com.openmpy.taleswiki.member.domain.repository;

import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.member.domain.MemberEmail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(final MemberEmail email);
}

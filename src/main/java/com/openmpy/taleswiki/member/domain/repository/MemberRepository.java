package com.openmpy.taleswiki.member.domain.repository;

import com.openmpy.taleswiki.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}

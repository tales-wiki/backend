package com.openmpy.taleswiki.member.application;

import com.openmpy.taleswiki.common.exception.CustomErrorCode;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.member.domain.MemberEmail;
import com.openmpy.taleswiki.member.domain.MemberSocial;
import com.openmpy.taleswiki.member.domain.repository.MemberRepository;
import com.openmpy.taleswiki.member.presentation.response.MemberLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberLoginResponse join(final String email, final MemberSocial social) {
        final MemberEmail memberEmail = new MemberEmail(email);

        if (memberRepository.existsByEmail(memberEmail)) {
            throw new CustomException(CustomErrorCode.ALREADY_SIGNUP_MEMBER, email);
        }

        final Member member = Member.create(email, social);
        memberRepository.save(member);
        return MemberLoginResponse.of(member);
    }
}

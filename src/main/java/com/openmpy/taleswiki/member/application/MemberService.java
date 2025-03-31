package com.openmpy.taleswiki.member.application;

import com.openmpy.taleswiki.auth.jwt.JwtTokenProvider;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.member.domain.MemberEmail;
import com.openmpy.taleswiki.member.domain.MemberSocial;
import com.openmpy.taleswiki.member.domain.repository.MemberRepository;
import com.openmpy.taleswiki.member.presentation.response.MemberLoginResponse;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public MemberLoginResponse join(final String email, final MemberSocial social) {
        final MemberEmail memberEmail = new MemberEmail(email);

        final Optional<Member> member = memberRepository.findByEmail(memberEmail);

        if (member.isPresent()) {
            return MemberLoginResponse.of(member.get());
        }

        final Member newMember = Member.create(email, social);
        final Member savedMember = memberRepository.save(newMember);
        return MemberLoginResponse.of(savedMember);
    }

    public String generateToken(final MemberLoginResponse response) {
        final Map<String, Object> payload = Map.of("id", response.id());
        return jwtTokenProvider.createToken(payload);
    }
}

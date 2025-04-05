package com.openmpy.taleswiki.member.application;

import static com.openmpy.taleswiki.auth.jwt.JwtTokenProvider.ID_KEY;
import static com.openmpy.taleswiki.auth.jwt.JwtTokenProvider.ROLE_KEY;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_MEMBER_ID;

import com.openmpy.taleswiki.auth.jwt.JwtTokenProvider;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.discord.application.DiscordService;
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
    private final DiscordService discordService;

    @Transactional
    public MemberLoginResponse join(final String email, final MemberSocial social) {
        final MemberEmail memberEmail = new MemberEmail(email);
        final Optional<Member> member = memberRepository.findByEmail(memberEmail);

        if (member.isPresent()) {
            return MemberLoginResponse.of(member.get());
        }

        final Member newMember = Member.create(email, social);
        final Member savedMember = memberRepository.save(newMember);

        discordService.sendSignupMessage(savedMember.getId(), email, social);
        return MemberLoginResponse.of(savedMember);
    }

    public Member getMember(final Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER_ID));
    }

    public String generateToken(final MemberLoginResponse response) {
        final Map<String, Object> payload = Map.of(
                ID_KEY, response.id(),
                ROLE_KEY, response.role()
        );
        return jwtTokenProvider.createToken(payload);
    }
}

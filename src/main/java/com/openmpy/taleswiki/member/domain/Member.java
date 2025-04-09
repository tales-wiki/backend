package com.openmpy.taleswiki.member.domain;

import com.openmpy.taleswiki.common.domain.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", unique = true, nullable = false))
    private MemberEmail email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberSocial social;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberAuthority authority;

    public Member(final Long id, final String email, final MemberSocial social, final MemberAuthority authority) {
        this.id = id;
        this.email = new MemberEmail(email);
        this.social = social;
        this.authority = authority;
    }

    @Builder
    public Member(final String email, final MemberSocial social, final MemberAuthority authority) {
        this.email = new MemberEmail(email);
        this.social = social;
        this.authority = authority;
    }

    public static Member create(final String email, final MemberSocial social) {
        return Member.builder()
                .email(email)
                .social(social)
                .authority(MemberAuthority.ADMIN)
                .build();
    }

    public String getEmail() {
        return email.getValue();
    }
}

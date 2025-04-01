package com.openmpy.taleswiki.member.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoLoginResponse(

        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("id_token")
        String idToken
) {
}

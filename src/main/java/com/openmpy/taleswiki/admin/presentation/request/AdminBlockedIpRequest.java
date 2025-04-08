package com.openmpy.taleswiki.admin.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record AdminBlockedIpRequest(

        @NotBlank(message = "IP 주소를 입력해주시길 바랍니다.")
        String ip
) {
}

package com.openmpy.taleswiki.admin.presentation;

import com.openmpy.taleswiki.admin.application.AdminQueryService;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionReportResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllBlockedIpResponses;
import com.openmpy.taleswiki.common.presentation.response.PaginatedResponse;
import com.openmpy.taleswiki.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminQueryController {

    private final AdminQueryService adminQueryService;

    @GetMapping("/members")
    public ResponseEntity<PaginatedResponse<Member>> readAllMember(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size
    ) {
        final PaginatedResponse<Member> response = adminQueryService.readAllMember(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/articles/versions")
    public ResponseEntity<PaginatedResponse<AdminReadAllArticleVersionResponse>> readAllArticleVersion(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size
    ) {
        final PaginatedResponse<AdminReadAllArticleVersionResponse> response =
                adminQueryService.readAllArticleVersion(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/articles/versions/reports")
    public ResponseEntity<PaginatedResponse<AdminReadAllArticleVersionReportResponse>> readAllArticleVersionReport(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size
    ) {
        final PaginatedResponse<AdminReadAllArticleVersionReportResponse> response =
                adminQueryService.readAllArticleVersionReport(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ip-block")
    public ResponseEntity<AdminReadAllBlockedIpResponses> readAllBlockedIp(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size
    ) {
        final AdminReadAllBlockedIpResponses responses = adminQueryService.readAllBlockedIp(page, size);
        return ResponseEntity.ok(responses);
    }
}

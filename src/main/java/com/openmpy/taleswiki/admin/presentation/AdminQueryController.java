package com.openmpy.taleswiki.admin.presentation;

import com.openmpy.taleswiki.admin.application.AdminQueryService;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionReportResponses;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionResponses;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllBlockedIpResponses;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllMemberResponses;
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
    public ResponseEntity<AdminReadAllMemberResponses> readAllMember(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size
    ) {
        final AdminReadAllMemberResponses responses = adminQueryService.readAllMember(page, size);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/articles/versions")
    public ResponseEntity<AdminReadAllArticleVersionResponses> readAllArticleVersion(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size
    ) {
        final AdminReadAllArticleVersionResponses responses = adminQueryService.readAllArticleVersion(page, size);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/articles/versions/reports")
    public ResponseEntity<AdminReadAllArticleVersionReportResponses> readAllArticleVersionReport(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size
    ) {
        final AdminReadAllArticleVersionReportResponses responses =
                adminQueryService.readAllArticleVersionReport(page, size);
        return ResponseEntity.ok(responses);
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

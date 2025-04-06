package com.openmpy.taleswiki.report.presentation;

import com.openmpy.taleswiki.report.application.ReportService;
import com.openmpy.taleswiki.report.presentation.request.ArticleReportRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/reports")
@RestController
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/article-versions/{versionId}")
    public ResponseEntity<Void> reportArticle(
            @PathVariable final Long versionId,
            @RequestBody final ArticleReportRequest request,
            final HttpServletRequest servletRequest
    ) {
        reportService.articleReport(versionId, request, servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

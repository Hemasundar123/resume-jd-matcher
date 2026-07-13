package com.hemasundar.matcher.controller;

import com.hemasundar.matcher.dto.MatchRequest;
import com.hemasundar.matcher.dto.MatchResponse;
import com.hemasundar.matcher.model.DocumentEntity;
import com.hemasundar.matcher.model.DocumentType;
import com.hemasundar.matcher.model.MatchReportEntity;
import com.hemasundar.matcher.service.DocumentService;
import com.hemasundar.matcher.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST entry points for creating documents and running a resume↔JD match.
 * Mirrors the API table in README.md.
 */
@RestController
@RequestMapping("/api/v1/match")
public class MatchController {

    private final DocumentService documentService;
    private final MatchService matchService;

    public MatchController(DocumentService documentService, MatchService matchService) {
        this.documentService = documentService;
        this.matchService = matchService;
    }

    /** POST /api/v1/match — resume text + JD text → match report. */
    @PostMapping
    public ResponseEntity<MatchResponse> matchFromText(@Valid @RequestBody MatchRequest request) {
        DocumentEntity resume = documentService.createFromText(
                DocumentType.RESUME, request.resumeTitle(), request.resumeText());
        DocumentEntity jd = documentService.createFromText(
                DocumentType.JOB_DESCRIPTION, request.jdTitle(), request.jdText());

        MatchReportEntity report = matchService.match(resume.getId(), jd.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(MatchResponse.from(report));
    }

    /** POST /api/v1/match/upload — resume PDF + JD text (multipart) → match report. */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<MatchResponse> matchFromUpload(
            @RequestParam("resume") MultipartFile resumeFile,
            @RequestParam("jdText") String jdText,
            @RequestParam(value = "resumeTitle", required = false) String resumeTitle,
            @RequestParam(value = "jdTitle", required = false) String jdTitle) {

        DocumentEntity resume = documentService.createFromFile(
                DocumentType.RESUME, resumeTitle, resumeFile);
        DocumentEntity jd = documentService.createFromText(
                DocumentType.JOB_DESCRIPTION, jdTitle, jdText);

        MatchReportEntity report = matchService.match(resume.getId(), jd.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(MatchResponse.from(report));
    }

    /** GET /api/v1/match/{id} — retrieve a stored report. */
    @GetMapping("/{id}")
    public MatchResponse getReport(@PathVariable Long id) {
        return MatchResponse.from(matchService.get(id));
    }
}
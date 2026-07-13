package com.hemasundar.matcher.dto;

import com.hemasundar.matcher.model.MatchReportEntity;
import java.time.OffsetDateTime;

/**
 * API-facing shape of a match report. Never expose MatchReportEntity
 * directly — this is the DTO boundary the CLAUDE.md conventions require.
 */
public record MatchResponse(
        Long id,
        Long resumeId,
        Long jdId,
        double similarity,
        double similarityPercent,
        String matchingSkills,
        String gaps,
        String suggestions,
        OffsetDateTime createdAt
) {
    public static MatchResponse from(MatchReportEntity e) {
        return new MatchResponse(
                e.getId(),
                e.getResumeId(),
                e.getJdId(),
                e.getSimilarity(),
                Math.round(e.getSimilarity() * 1000) / 10.0, // one decimal %
                e.getMatchingSkills(),
                e.getGaps(),
                e.getSuggestions(),
                e.getCreatedAt()
        );
    }
}
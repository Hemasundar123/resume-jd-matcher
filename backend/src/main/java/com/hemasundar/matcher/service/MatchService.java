package com.hemasundar.matcher.service;

import com.hemasundar.matcher.dto.MatchFeedback;
import com.hemasundar.matcher.model.DocumentEntity;
import com.hemasundar.matcher.model.MatchReportEntity;
import com.hemasundar.matcher.repository.MatchReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Orchestrates the end-to-end match: similarity score from embeddings
 * plus Claude-generated qualitative feedback, persisted as a report.
 */
@Service
public class MatchService {

    private final DocumentService documentService;
    private final EmbeddingService embeddingService;
    private final FeedbackService feedbackService;
    private final MatchReportRepository matchReportRepository;

    public MatchService(DocumentService documentService,
                        EmbeddingService embeddingService,
                        FeedbackService feedbackService,
                        MatchReportRepository matchReportRepository) {
        this.documentService = documentService;
        this.embeddingService = embeddingService;
        this.feedbackService = feedbackService;
        this.matchReportRepository = matchReportRepository;
    }

    /**
     * Match a resume document against a JD document and persist the report.
     *
     * @param resumeId id of a stored RESUME document
     * @param jdId     id of a stored JOB_DESCRIPTION document
     * @return the saved match report
     */
    @Transactional
    public MatchReportEntity match(Long resumeId, Long jdId) {
        DocumentEntity resume = documentService.get(resumeId);
        DocumentEntity jd = documentService.get(jdId);

        // 1. Semantic similarity from the stored embeddings
        float[] resumeVec = parse(resume.getEmbedding());
        float[] jdVec = parse(jd.getEmbedding());
        double similarity = EmbeddingService.cosineSimilarity(resumeVec, jdVec);

        // 2. Qualitative feedback from Claude
        MatchFeedback feedback =
                feedbackService.analyze(resume.getContent(), jd.getContent());

        // 3. Persist the report
        MatchReportEntity report =
                new MatchReportEntity(resumeId, jdId, similarity);
        report.setMatchingSkills(feedback.matchingSkills());
        report.setGaps(feedback.gaps());
        report.setSuggestions(feedback.suggestions());
        return matchReportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public MatchReportEntity get(Long id) {
        return matchReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Match report not found: " + id));
    }

    /** Parse a pgvector literal "[0.1,0.2,...]" back into a float[]. */
    private float[] parse(String literal) {
        if (literal == null || literal.length() < 2) {
            throw new DocumentProcessingException("Missing embedding");
        }
        String inner = literal.substring(1, literal.length() - 1); // strip [ ]
        String[] parts = inner.split(",");
        float[] out = new float[parts.length];
        for (int i = 0; i < parts.length; i++) {
            out[i] = Float.parseFloat(parts[i].trim());
        }
        return out;
    }
}

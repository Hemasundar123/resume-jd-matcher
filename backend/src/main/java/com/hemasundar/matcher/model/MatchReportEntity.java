package com.hemasundar.matcher.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;


@Entity
@Table(name = "match_reports")
public class MatchReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resume_id", nullable = false)
    private Long resumeId;

    @Column(name = "jd_id", nullable = false)
    private Long jdId;

    @Column(nullable = false)
    private double similarity;

    @Column(name = "matching_skills", columnDefinition = "text")
    private String matchingSkills;

    @Column(columnDefinition = "text")
    private String gaps;

    @Column(columnDefinition = "text")
    private String suggestions;

    @Column(name = "created_at", nullable = false, updatable = false,
            insertable = false)
    private OffsetDateTime createdAt;

    protected MatchReportEntity() { }   // JPA

    public MatchReportEntity(Long resumeId, Long jdId, double similarity) {
        this.resumeId = resumeId;
        this.jdId = jdId;
        this.similarity = similarity;
    }

    public Long getId() { return id; }
    public Long getResumeId() { return resumeId; }
    public Long getJdId() { return jdId; }
    public double getSimilarity() { return similarity; }
    public void setSimilarity(double similarity) { this.similarity = similarity; }
    public String getMatchingSkills() { return matchingSkills; }
    public void setMatchingSkills(String v) { this.matchingSkills = v; }
    public String getGaps() { return gaps; }
    public void setGaps(String v) { this.gaps = v; }
    public String getSuggestions() { return suggestions; }
    public void setSuggestions(String v) { this.suggestions = v; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
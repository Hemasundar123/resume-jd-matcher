package com.hemasundar.matcher.dto;

/**
 * Structured qualitative feedback produced by Claude when comparing
 * a resume to a job description.
 */
public record MatchFeedback(
        String matchingSkills,
        String gaps,
        String suggestions
) { }
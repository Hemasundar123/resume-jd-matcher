package com.hemasundar.matcher.dto;

import jakarta.validation.constraints.NotBlank;


public record MatchRequest(
        String resumeTitle,
        @NotBlank(message = "resumeText must not be blank") String resumeText,
        String jdTitle,
        @NotBlank(message = "jdText must not be blank") String jdText
) { }
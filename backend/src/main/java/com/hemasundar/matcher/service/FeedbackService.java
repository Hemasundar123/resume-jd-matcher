package com.hemasundar.matcher.service;

import com.hemasundar.matcher.dto.MatchFeedback;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * Uses the configured chat model (via Spring AI) to generate qualitative
 * feedback comparing a resume against a job description.
 */
@Service
public class FeedbackService {

    private final ChatClient chatClient;

    public FeedbackService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    private static final String SYSTEM_PROMPT = """
        You are an expert technical recruiter. Compare the candidate's resume
        to the job description. Be concise, specific, and honest. Base your
        analysis only on the provided texts. Do not invent experience.
        """;

    private static final String USER_TEMPLATE = """
        RESUME:
        {resume}

        JOB DESCRIPTION:
        {jd}

        Analyze the fit. Return:
        - matchingSkills: skills/experience in the resume that match the JD
        - gaps: important JD requirements the resume does not clearly show
        - suggestions: concrete ways the candidate could improve their fit

        Respond with ONLY a single valid JSON object with exactly these three
        keys: matchingSkills, gaps, suggestions. Keep each value under 40 words.
        Do not include any text before or after the JSON. Ensure the JSON is
        complete and properly closed with a final closing brace.
        """;

    /**
     * Ask the chat model to compare a resume and JD and return structured feedback.
     */
    public MatchFeedback analyze(String resumeText, String jdText) {
        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(u -> u.text(USER_TEMPLATE)
                        .param("resume", truncate(resumeText))
                        .param("jd", truncate(jdText)))
                .call()
                .entity(MatchFeedback.class);
    }

    /** Guard against oversized prompts; keep within a sane token budget. */
    private String truncate(String text) {
        int max = 8000;
        return text.length() <= max ? text : text.substring(0, max);
    }
}

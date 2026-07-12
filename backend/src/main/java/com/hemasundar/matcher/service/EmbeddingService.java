package com.hemasundar.matcher.service;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.StringJoiner;


@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    public EmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    /**
     * Embed text into a float vector.
     *
     * @param text input text
     * @return the embedding as a float array
     */
    public float[] embed(String text) {
        if (text == null || text.isBlank()) {
            throw new DocumentProcessingException("Cannot embed empty text");
        }
        return embeddingModel.embed(text);
    }

    /**
     * Embed text into a vector literal string, e.g. "[0.1,0.2,0.3]".
     *
     * @param text input text
     * @return the embedding as a vector literal string
     */
    public String embedAsVectorLiteral(String text) {
        float[] vector = embed(text);
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (float v : vector) {
            joiner.add(Float.toString(v));
        }
        return joiner.toString();
    }

    /** Cosine similarity between two vectors, range roughly -1..1 (0..1 in practice). */
    public static double cosineSimilarity(float[] a, float[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Vector length mismatch");
        }
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        if (normA == 0 || normB == 0) return 0.0;
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
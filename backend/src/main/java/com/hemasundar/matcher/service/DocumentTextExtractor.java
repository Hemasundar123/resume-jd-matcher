package com.hemasundar.matcher.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Service
public class DocumentTextExtractor {

    /**
     * Extract text from a multipart upload.
     *
     * @param file the uploaded file (PDF or text)
     * @return normalized plain text
     */
    public String extract(MultipartFile file) {
        try {
            String name = file.getOriginalFilename();
            if (name != null && name.toLowerCase().endsWith(".pdf")) {
                return extractPdf(file.getBytes());
            }
            return normalize(new String(file.getBytes(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new DocumentProcessingException(
                    "Failed to read uploaded file", e);
        }
    }

    private String extractPdf(byte[] bytes) throws IOException {
        try (PDDocument doc = Loader.loadPDF(bytes)) {
            String raw = new PDFTextStripper().getText(doc);
            return normalize(raw);
        }
    }

    
    private String normalize(String text) {
        if (text == null) return "";
        return text.replaceAll("\\s+", " ").trim();
    }
}
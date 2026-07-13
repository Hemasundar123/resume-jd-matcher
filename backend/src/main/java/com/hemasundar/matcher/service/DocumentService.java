package com.hemasundar.matcher.service;

import com.hemasundar.matcher.model.DocumentEntity;
import com.hemasundar.matcher.model.DocumentType;
import com.hemasundar.matcher.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Application service for creating and storing documents together
 * with their semantic embeddings.
 */
@Service
public class DocumentService {

    private final DocumentTextExtractor extractor;
    private final EmbeddingService embeddingService;
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentTextExtractor extractor,
                           EmbeddingService embeddingService,
                           DocumentRepository documentRepository) {
        this.extractor = extractor;
        this.embeddingService = embeddingService;
        this.documentRepository = documentRepository;
    }

    /** Create a document from raw text. */
    @Transactional
    public DocumentEntity createFromText(DocumentType type, String title, String content) {
        DocumentEntity entity = new DocumentEntity(type, title, content);
        entity.setEmbedding(embeddingService.embedAsVectorLiteral(content));
        return documentRepository.save(entity);
    }

    /** Create a document from an uploaded file (PDF/text). */
    @Transactional
    public DocumentEntity createFromFile(DocumentType type, String title, MultipartFile file) {
        String content = extractor.extract(file);
        return createFromText(type, title, content);
    }

    @Transactional(readOnly = true)
    public DocumentEntity get(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Document not found: " + id));
    }
}
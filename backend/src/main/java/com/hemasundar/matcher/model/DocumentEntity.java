package com.hemasundar.matcher.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import org.hibernate.annotations.ColumnTransformer;


@Entity
@Table(name = "documents")
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DocumentType type;

    @Column(length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    /**
     * Embedding vector, stored in the pgvector `embedding` column.
     * Mapped as a String in JPA (pgvector accepts the textual form
     * "[0.1,0.2,...]"); the service layer converts to/from float[].
     */
    @Column(columnDefinition = "vector(384)")
    @ColumnTransformer(write = "?::vector")
    private String embedding;

    @Column(name = "created_at", nullable = false, updatable = false,
            insertable = false)
    private OffsetDateTime createdAt;

    protected DocumentEntity() { }   // JPA

    public DocumentEntity(DocumentType type, String title, String content) {
        this.type = type;
        this.title = title;
        this.content = content;
    }

    
    public Long getId() { return id; }
    public DocumentType getType() { return type; }
    public void setType(DocumentType type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getEmbedding() { return embedding; }
    public void setEmbedding(String embedding) { this.embedding = embedding; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
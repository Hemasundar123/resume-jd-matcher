CREATE TABLE documents (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type          VARCHAR(20)  NOT NULL,          
    title         VARCHAR(255),
    content       TEXT         NOT NULL,
    embedding     vector(384),                    
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);


CREATE INDEX idx_documents_embedding
    ON documents
    USING hnsw (embedding vector_cosine_ops);

CREATE INDEX idx_documents_type ON documents (type);
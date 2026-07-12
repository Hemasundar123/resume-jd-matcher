package com.hemasundar.matcher.repository;

import com.hemasundar.matcher.model.DocumentEntity;
import com.hemasundar.matcher.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    List<DocumentEntity> findByType(DocumentType type);
}

package com.hemasundar.matcher.repository;

import com.hemasundar.matcher.model.MatchReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchReportRepository
        extends JpaRepository<MatchReportEntity, Long> {
}
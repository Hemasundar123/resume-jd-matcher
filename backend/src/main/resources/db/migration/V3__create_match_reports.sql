CREATE TABLE match_reports (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    resume_id       BIGINT       NOT NULL REFERENCES documents (id),
    jd_id           BIGINT       NOT NULL REFERENCES documents (id),
    similarity      DOUBLE PRECISION NOT NULL,     
    matching_skills TEXT,                          
    gaps            TEXT,                          
    suggestions     TEXT,                          
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_match_reports_resume ON match_reports (resume_id);
CREATE INDEX idx_match_reports_jd     ON match_reports (jd_id);
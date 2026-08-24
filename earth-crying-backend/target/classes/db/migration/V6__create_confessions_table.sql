-- Flyway Migration: V6__create_confessions_table.sql
-- Confessions table for anonymous admission wall

CREATE TABLE confessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    content TEXT NOT NULL,
    impact_category impact_category,
    is_approved BOOLEAN NOT NULL DEFAULT FALSE,
    is_flagged BOOLEAN NOT NULL DEFAULT FALSE,
    flag_reason VARCHAR(200),
    submitted_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    approved_at TIMESTAMPTZ,
    approved_by UUID REFERENCES users(id) ON DELETE SET NULL,
    ip_hash VARCHAR(64),
    user_agent TEXT
);

CREATE INDEX idx_confessions_is_approved ON confessions(is_approved);
CREATE INDEX idx_confessions_is_flagged ON confessions(is_flagged);
CREATE INDEX idx_confessions_impact_category ON confessions(impact_category);
CREATE INDEX idx_confessions_submitted_at ON confessions(submitted_at DESC);
CREATE INDEX idx_confessions_ip_hash ON confessions(ip_hash);
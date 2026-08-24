-- Flyway Migration: V8__add_grace_fields_to_pledges.sql
-- Add grace period fields to pledges table for streak grace system

ALTER TABLE pledges 
ADD COLUMN IF NOT EXISTS grace_days_remaining INT NOT NULL DEFAULT 1,
ADD COLUMN IF NOT EXISTS last_missed_check_in_at TIMESTAMPTZ,
ADD COLUMN IF NOT EXISTS consecutive_missed_days INT NOT NULL DEFAULT 0;
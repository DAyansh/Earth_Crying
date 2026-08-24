-- Flyway Migration: V1__initial_schema.sql
-- Initial database schema for Earth Crying

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Enum types
CREATE TYPE impact_category AS ENUM (
    'DIGITAL_CARBON_FOOTPRINT',
    'TIRE_BRAKE_DUST',
    'FAST_FASHION_MICROPLASTICS',
    'LIGHT_POLLUTION',
    'UNDERWATER_NOISE_POLLUTION',
    'FERTILIZER_RUNOFF',
    'E_WASTE_RARE_EARTH_MINING',
    'HIDDEN_WATER_FOOTPRINT',
    'PALM_OIL_SUPPLY_CHAINS',
    'SPACE_DEBRIS_SATELLITE_POLLUTION',
    'SOIL_EROSION_MONOCULTURE',
    'INDOOR_VOCS_FRAGRANCE_CHEMICALS'
);

CREATE TYPE effort_level AS ENUM (
    'ONE_MINUTE',
    'WEEKLY_HABIT',
    'LIFESTYLE_CHANGE'
);

CREATE TYPE action_scale AS ENUM (
    'INDIVIDUAL',
    'COMMUNITY',
    'POLICY'
);

CREATE TYPE pledge_status AS ENUM (
    'ACTIVE',
    'COMPLETED',
    'PAUSED',
    'ABANDONED'
);

CREATE TYPE solution_status AS ENUM (
    'PENDING',
    'APPROVED',
    'REJECTED',
    'FLAGGED'
);

-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(100),
    avatar_url VARCHAR(500),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_login_at TIMESTAMPTZ
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_created_at ON users(created_at);

-- Impacts table (the 12 hidden damage categories)
CREATE TABLE impacts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    category impact_category NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    hidden_stat VARCHAR(500) NOT NULL,
    explanation TEXT NOT NULL,
    why_invisible TEXT NOT NULL,
    video_asset_url VARCHAR(500),
    thumbnail_url VARCHAR(500),
    hope_story TEXT NOT NULL,
    hope_story_title VARCHAR(200),
    hope_story_image_url VARCHAR(500),
    sources JSONB NOT NULL DEFAULT '[]',
    global_per_second_rate DECIMAL(20, 4),
    counter_unit VARCHAR(50),
    counter_label VARCHAR(100),
    display_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_impacts_category ON impacts(category);
CREATE INDEX idx_impacts_display_order ON impacts(display_order);
CREATE INDEX idx_impacts_is_active ON impacts(is_active);

-- Solutions/Actions table
CREATE TABLE solutions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    impact_id UUID NOT NULL REFERENCES impacts(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    effort_level effort_level NOT NULL,
    action_scale action_scale NOT NULL,
    impact_score INT NOT NULL DEFAULT 1,
    co2_saved_kg_per_year DECIMAL(10, 2),
    water_saved_liters_per_year DECIMAL(10, 2),
    money_saved_usd_per_year DECIMAL(10, 2),
    external_resource_url VARCHAR(500),
    display_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_solutions_impact_id ON solutions(impact_id);
CREATE INDEX idx_solutions_effort_level ON solutions(effort_level);
CREATE INDEX idx_solutions_action_scale ON solutions(action_scale);
CREATE INDEX idx_solutions_is_active ON solutions(is_active);

-- Hope Ledger entries
CREATE TABLE hope_ledger_entries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    metric_name VARCHAR(100) NOT NULL,
    metric_value DECIMAL(20, 4) NOT NULL,
    metric_unit VARCHAR(50) NOT NULL,
    region VARCHAR(100),
    country_code VARCHAR(2),
    source_name VARCHAR(100) NOT NULL,
    source_url VARCHAR(500),
    recorded_at TIMESTAMPTZ NOT NULL,
    fetched_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    is_latest BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_hope_ledger_category ON hope_ledger_entries(category);
CREATE INDEX idx_hope_ledger_recorded_at ON hope_ledger_entries(recorded_at DESC);
CREATE INDEX idx_hope_ledger_is_latest ON hope_ledger_entries(is_latest);
CREATE INDEX idx_hope_ledger_country_code ON hope_ledger_entries(country_code);

-- Geo Impact data for map/globe
CREATE TABLE geo_impacts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    country_code VARCHAR(2) NOT NULL,
    country_name VARCHAR(100) NOT NULL,
    region VARCHAR(100),
    deforestation_rate_hectares_per_year DECIMAL(15, 2),
    aqi_avg INT,
    water_stress_index DECIMAL(5, 2),
    co2_emissions_mt_per_year DECIMAL(15, 2),
    renewable_energy_percent DECIMAL(5, 2),
    protected_land_percent DECIMAL(5, 2),
    biodiversity_intactness_index DECIMAL(5, 2),
    plastic_waste_mt_per_year DECIMAL(15, 2),
    data_year INT NOT NULL,
    source_name VARCHAR(100) NOT NULL,
    source_url VARCHAR(500),
    fetched_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (country_code, data_year)
);

CREATE INDEX idx_geo_impacts_country_code ON geo_impacts(country_code);
CREATE INDEX idx_geo_impacts_data_year ON geo_impacts(data_year);

-- User Footprint Quiz Results
CREATE TABLE footprint_results (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    session_id VARCHAR(100),
    category_scores JSONB NOT NULL,
    total_score INT NOT NULL,
    top_categories JSONB NOT NULL,
    recommended_actions JSONB NOT NULL,
    completed_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_footprint_results_user_id ON footprint_results(user_id);
CREATE INDEX idx_footprint_results_completed_at ON footprint_results(completed_at DESC);

-- Pledges
CREATE TABLE pledges (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    solution_id UUID NOT NULL REFERENCES solutions(id) ON DELETE CASCADE,
    status pledge_status NOT NULL DEFAULT 'ACTIVE',
    started_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    completed_at TIMESTAMPTZ,
    target_date TIMESTAMPTZ,
    current_streak INT NOT NULL DEFAULT 0,
    longest_streak INT NOT NULL DEFAULT 0,
    last_check_in_at TIMESTAMPTZ,
    check_in_count INT NOT NULL DEFAULT 0,
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_pledges_user_id ON pledges(user_id);
CREATE INDEX idx_pledges_solution_id ON pledges(solution_id);
CREATE INDEX idx_pledges_status ON pledges(status);
CREATE INDEX idx_pledges_started_at ON pledges(started_at DESC);

-- Badges
CREATE TABLE badges (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    icon_url VARCHAR(500),
    category VARCHAR(50) NOT NULL,
    requirement_type VARCHAR(50) NOT NULL,
    requirement_value INT NOT NULL,
    rarity VARCHAR(20) NOT NULL DEFAULT 'COMMON',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_badges_code ON badges(code);
CREATE INDEX idx_badges_category ON badges(category);

-- User Badges
CREATE TABLE user_badges (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    badge_id UUID NOT NULL REFERENCES badges(id) ON DELETE CASCADE,
    earned_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    pledge_id UUID REFERENCES pledges(id) ON DELETE SET NULL,
    UNIQUE (user_id, badge_id)
);

CREATE INDEX idx_user_badges_user_id ON user_badges(user_id);
CREATE INDEX idx_user_badges_badge_id ON user_badges(badge_id);

-- Community Solutions Wall
CREATE TABLE community_solutions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(200),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    impact_category impact_category,
    related_solution_id UUID REFERENCES solutions(id) ON DELETE SET NULL,
    status solution_status NOT NULL DEFAULT 'PENDING',
    upvote_count INT NOT NULL DEFAULT 0,
    downvote_count INT NOT NULL DEFAULT 0,
    flag_count INT NOT NULL DEFAULT 0,
    moderated_at TIMESTAMPTZ,
    moderated_by UUID REFERENCES users(id) ON DELETE SET NULL,
    moderation_notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_community_solutions_user_id ON community_solutions(user_id);
CREATE INDEX idx_community_solutions_status ON community_solutions(status);
CREATE INDEX idx_community_solutions_impact_category ON community_solutions(impact_category);
CREATE INDEX idx_community_solutions_created_at ON community_solutions(created_at DESC);
CREATE INDEX idx_community_solutions_location ON community_solutions(latitude, longitude);

-- Community Solution Votes
CREATE TABLE community_solution_votes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    community_solution_id UUID NOT NULL REFERENCES community_solutions(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    vote_type VARCHAR(10) NOT NULL CHECK (vote_type IN ('UPVOTE', 'DOWNVOTE')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (community_solution_id, user_id)
);

CREATE INDEX idx_community_solution_votes_solution_id ON community_solution_votes(community_solution_id);
CREATE INDEX idx_community_solution_votes_user_id ON community_solution_votes(user_id);

-- Community Solution Flags
CREATE TABLE community_solution_flags (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    community_solution_id UUID NOT NULL REFERENCES community_solutions(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    reason VARCHAR(100) NOT NULL,
    details TEXT,
    resolved BOOLEAN NOT NULL DEFAULT FALSE,
    resolved_at TIMESTAMPTZ,
    resolved_by UUID REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_community_solution_flags_solution_id ON community_solution_flags(community_solution_id);
CREATE INDEX idx_community_solution_flags_user_id ON community_solution_flags(user_id);

-- Refresh Tokens
CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash VARCHAR(255) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_used_at TIMESTAMPTZ
);

CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_token_hash ON refresh_tokens(token_hash);
CREATE INDEX idx_refresh_tokens_expires_at ON refresh_tokens(expires_at);

-- Email Verification Tokens
CREATE TABLE email_verification_tokens (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash VARCHAR(255) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_email_verification_tokens_user_id ON email_verification_tokens(user_id);
CREATE INDEX idx_email_verification_tokens_token_hash ON email_verification_tokens(token_hash);

-- Password Reset Tokens
CREATE TABLE password_reset_tokens (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash VARCHAR(255) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_password_reset_tokens_user_id ON password_reset_tokens(user_id);
CREATE INDEX idx_password_reset_tokens_token_hash ON password_reset_tokens(token_hash);

-- Notification Preferences
CREATE TABLE notification_preferences (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE UNIQUE,
    email_digest BOOLEAN NOT NULL DEFAULT TRUE,
    email_streak_reminders BOOLEAN NOT NULL DEFAULT TRUE,
    email_earth_day BOOLEAN NOT NULL DEFAULT TRUE,
    email_world_env_day BOOLEAN NOT NULL DEFAULT TRUE,
    push_notifications BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Scheduled Jobs Log
CREATE TABLE scheduled_job_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    job_name VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('STARTED', 'SUCCESS', 'FAILED', 'PARTIAL')),
    started_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    completed_at TIMESTAMPTZ,
    records_processed INT,
    error_message TEXT,
    metadata JSONB
);

CREATE INDEX idx_scheduled_job_logs_job_name ON scheduled_job_logs(job_name);
CREATE INDEX idx_scheduled_job_logs_started_at ON scheduled_job_logs(started_at DESC);
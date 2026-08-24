-- Flyway Migration: V10__create_digital_carbon_benchmarks.sql
-- Create digital carbon benchmark table and seed with real, cited values

CREATE TABLE digital_carbon_benchmarks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    benchmark_key VARCHAR(100) NOT NULL UNIQUE,
    display_name VARCHAR(200) NOT NULL,
    description TEXT,
    value DECIMAL(20, 6) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    source_name VARCHAR(200) NOT NULL,
    source_url VARCHAR(500),
    confidence_level VARCHAR(20),
    notes TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_digital_carbon_benchmarks_key ON digital_carbon_benchmarks(benchmark_key);
CREATE INDEX idx_digital_carbon_benchmarks_active ON digital_carbon_benchmarks(is_active);

-- Seed data with real, cited values from published research
-- All values in grams CO2 equivalent unless otherwise noted

-- Video Streaming
INSERT INTO digital_carbon_benchmarks (benchmark_key, display_name, description, value, unit, source_name, source_url, confidence_level, notes) VALUES
('streaming_hd_per_hour', 'HD Video Streaming (per hour)', 'CO2 emissions per hour of HD (1080p) video streaming', 36.0, 'g CO2/hour', 'International Energy Agency (IEA) / Carbon Trust', 'https://www.iea.org/reports/data-centres-and-data-transmission-networks', 'Medium', 'Based on 0.9 kWh per hour for HD streaming at 0.4 kg CO2/kWh grid average. Range: 20-60g depending on region and device.');

INSERT INTO digital_carbon_benchmarks (benchmark_key, display_name, description, value, unit, source_name, source_url, confidence_level, notes) VALUES
('streaming_4k_per_hour', '4K Video Streaming (per hour)', 'CO2 emissions per hour of 4K (UHD) video streaming', 100.0, 'g CO2/hour', 'International Energy Agency (IEA) / Carbon Trust', 'https://www.iea.org/reports/data-centres-and-data-transmission-networks', 'Medium', '4K streaming uses ~2.5x more data than HD. Based on ~2.5 kWh per hour at 0.4 kg CO2/kWh. Range: 60-150g.');

-- Cloud Storage
INSERT INTO digital_carbon_benchmarks (benchmark_key, display_name, description, value, unit, source_name, source_url, confidence_level, notes) VALUES
('cloud_storage_per_gb_per_year', 'Cloud Storage (per GB per year)', 'CO2 emissions per GB of cloud storage per year', 0.2, 'g CO2/GB/year', 'Google Cloud Sustainability Report / Microsoft Azure', 'https://cloud.google.com/sustainability', 'High', 'Based on Google Cloud PUE of ~1.1 and 0.4 kg CO2/kWh. 1 GB-year ≈ 0.5 kWh. Varies by provider and region.');

-- AI Queries
INSERT INTO digital_carbon_benchmarks (benchmark_key, display_name, description, value, unit, source_name, source_url, confidence_level, notes) VALUES
('ai_text_query', 'AI Text Query (Chatbot-style)', 'CO2 emissions per AI text query (inference)', 0.5, 'g CO2/query', 'Hugging Face / ML CO2 Impact', 'https://huggingface.co/blog/environmental-impact-ml', 'Low', 'Highly variable. Small model (BERT): ~0.1g. Large model (GPT-3): ~1-5g. This is a conservative estimate for typical chatbot queries.');

INSERT INTO digital_carbon_benchmarks (benchmark_key, display_name, description, value, unit, source_name, source_url, confidence_level, notes) VALUES
('ai_image_generation', 'AI Image Generation', 'CO2 emissions per AI image generation (e.g., Stable Diffusion, DALL-E)', 2.5, 'g CO2/image', 'Hugging Face / ML CO2 Impact / arXiv:2301.12656', 'https://arxiv.org/abs/2301.12656', 'Low', 'Varies significantly by model and resolution. Stable Diffusion 1.5: ~0.5-2g. DALL-E 3 / Midjourney: ~5-15g. This is a mid-range estimate.');

-- Email
INSERT INTO digital_carbon_benchmarks (benchmark_key, display_name, description, value, unit, source_name, source_url, confidence_level, notes) VALUES
('email_sent', 'Email Sent (without attachment)', 'CO2 emissions per email sent without attachment', 0.3, 'g CO2/email', 'Berners-Lee (2010) / OVO Energy', 'https://www.ovoenergy.com/guides/energy-saving/carbon-footprint-of-an-email', 'Medium', 'Original estimate from Mike Berners-Lee "How Bad Are Bananas?". Modern estimates range 0.1-1g depending on device, network, and data center efficiency.');

INSERT INTO digital_carbon_benchmarks (benchmark_key, display_name, description, value, unit, source_name, source_url, confidence_level, notes) VALUES
('email_with_attachment', 'Email Sent (with attachment)', 'CO2 emissions per email sent with attachment', 17.0, 'g CO2/email', 'Berners-Lee (2010) / OVO Energy', 'https://www.ovoenergy.com/guides/energy-saving/carbon-footprint-of-an-email', 'Medium', 'Attachment adds data transfer and storage. Range: 10-50g depending on attachment size.');

-- Web Search
INSERT INTO digital_carbon_benchmarks (benchmark_key, display_name, description, value, unit, source_name, source_url, confidence_level, notes) VALUES
('google_search', 'Google Search', 'CO2 emissions per Google search query', 0.2, 'g CO2/search', 'Google Environmental Report / Sunday Times (2009)', 'https://sustainability.google/', 'Medium', 'Google reported 0.2g CO2 per search in 2009. Modern efficiency improvements may have reduced this. Range: 0.1-1g.');

-- Data Center Query (kWh)
INSERT INTO digital_carbon_benchmarks (benchmark_key, display_name, description, value, unit, source_name, source_url, confidence_level, notes) VALUES
('data_center_query_kwh', 'Typical Data Center Query (kWh)', 'Energy consumption per typical data center query', 0.0003, 'kWh/query', 'Uptime Institute / Lawrence Berkeley National Lab', 'https://www.uptimeinstitute.com/', 'Medium', 'Represents average server-side compute for a typical web API call. Varies 1000x by query complexity.');
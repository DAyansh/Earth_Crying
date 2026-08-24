-- Flyway Migration: V7__add_country_benchmarks.sql
-- Country benchmark data for footprint comparison engine

CREATE TABLE country_benchmarks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    country_code VARCHAR(2) NOT NULL UNIQUE,
    country_name VARCHAR(100) NOT NULL,
    avg_co2_per_capita_tons DECIMAL(10, 2) NOT NULL,
    avg_water_footprint_liters_per_day DECIMAL(10, 2) NOT NULL,
    avg_digital_carbon_kg_per_year DECIMAL(10, 2) NOT NULL,
    avg_transport_emissions_kg_per_year DECIMAL(10, 2) NOT NULL,
    avg_fashion_impact_score DECIMAL(10, 2) NOT NULL,
    avg_e_waste_kg_per_year DECIMAL(10, 2) NOT NULL,
    avg_indoor_vocs_score DECIMAL(10, 2) NOT NULL,
    sustainable_target_co2_per_capita_tons DECIMAL(10, 2) NOT NULL,
    sustainable_target_water_liters_per_day DECIMAL(10, 2) NOT NULL,
    data_year INT NOT NULL,
    source_name VARCHAR(200) NOT NULL,
    source_url VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_country_benchmarks_country_code ON country_benchmarks(country_code);

-- Insert benchmark data for major countries (2023 data from Global Carbon Project, Water Footprint Network, etc.)
INSERT INTO country_benchmarks (
    country_code, country_name, 
    avg_co2_per_capita_tons, avg_water_footprint_liters_per_day, 
    avg_digital_carbon_kg_per_year, avg_transport_emissions_kg_per_year,
    avg_fashion_impact_score, avg_e_waste_kg_per_year, avg_indoor_vocs_score,
    sustainable_target_co2_per_capita_tons, sustainable_target_water_liters_per_day,
    data_year, source_name, source_url
) VALUES 
('US', 'United States', 14.9, 7800, 350, 4500, 65, 21, 40, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org'),
('CN', 'China', 8.0, 2900, 180, 1200, 45, 15, 35, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org'),
('IN', 'India', 1.9, 2500, 60, 300, 30, 5, 25, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org'),
('RU', 'Russia', 11.5, 4200, 220, 2800, 40, 12, 38, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org'),
('JP', 'Japan', 8.4, 3800, 280, 1800, 55, 18, 45, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org'),
('DE', 'Germany', 7.7, 3900, 320, 2100, 60, 22, 42, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org'),
('BR', 'Brazil', 2.3, 4500, 120, 1100, 35, 8, 30, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org'),
('CA', 'Canada', 14.2, 6800, 340, 4200, 58, 20, 40, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org'),
('AU', 'Australia', 15.1, 5500, 310, 3800, 52, 19, 38, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org'),
('FR', 'France', 4.5, 3500, 290, 1900, 50, 18, 38, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org'),
('GB', 'United Kingdom', 5.2, 3400, 300, 2000, 55, 23, 40, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org'),
('KR', 'South Korea', 11.8, 3200, 270, 2200, 50, 16, 45, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org'),
('IT', 'Italy', 5.3, 3800, 250, 1800, 58, 17, 42, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org'),
('MX', 'Mexico', 3.3, 3600, 150, 1400, 40, 10, 35, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org'),
('ID', 'Indonesia', 2.3, 2200, 80, 600, 28, 6, 28, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org'),
('ZZ', 'Global Average', 4.7, 3800, 200, 1500, 42, 12, 35, 2.3, 3000, 2023, 'Global Carbon Project / Water Footprint Network', 'https://globalcarbonproject.org');
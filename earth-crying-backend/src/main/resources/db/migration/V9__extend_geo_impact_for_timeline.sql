-- Flyway Migration: V9__extend_geo_impact_for_timeline.sql
-- Extend geo_impacts for time-travel timeline support (historical + projected data)

ALTER TABLE geo_impacts 
ADD COLUMN IF NOT EXISTS is_projected BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS projection_scenario VARCHAR(50),
ADD COLUMN IF NOT EXISTS confidence_level DECIMAL(3, 2);

-- Add index for timeline queries
CREATE INDEX IF NOT EXISTS idx_geo_impacts_timeline ON geo_impacts(country_code, data_year, is_projected);

-- Insert historical data (past years)
INSERT INTO geo_impacts (
    country_code, country_name, region,
    deforestation_rate_hectares_per_year, aqi_avg, water_stress_index,
    co2_emissions_mt_per_year, renewable_energy_percent, protected_land_percent,
    biodiversity_intactness_index, plastic_waste_mt_per_year,
    data_year, source_name, source_url, is_projected
) VALUES 
-- Historical data for US
('US', 'United States', 'North America', 350000, 45, 2.8, 5200, 21.5, 12.1, 0.65, 42, 2010, 'World Bank / FAO', 'https://worldbank.org', FALSE),
('US', 'United States', 'North America', 320000, 42, 3.1, 5100, 18.2, 12.3, 0.63, 38, 2015, 'World Bank / FAO', 'https://worldbank.org', FALSE),
('US', 'United States', 'North America', 280000, 38, 3.4, 4900, 21.5, 12.1, 0.65, 42, 2020, 'World Bank / FAO', 'https://worldbank.org', FALSE),

-- Historical data for CN
('CN', 'China', 'East Asia', 280000, 85, 4.2, 9800, 15.3, 17.2, 0.45, 60, 2010, 'World Bank / FAO', 'https://worldbank.org', FALSE),
('CN', 'China', 'East Asia', 220000, 72, 4.5, 10500, 25.1, 18.5, 0.48, 55, 2015, 'World Bank / FAO', 'https://worldbank.org', FALSE),
('CN', 'China', 'East Asia', 180000, 58, 4.8, 11200, 31.2, 18.8, 0.52, 52, 2020, 'World Bank / FAO', 'https://worldbank.org', FALSE),

-- Historical data for BR
('BR', 'Brazil', 'South America', 2100000, 35, 1.8, 450, 45.2, 29.5, 0.72, 12, 2010, 'World Bank / FAO', 'https://worldbank.org', FALSE),
('BR', 'Brazil', 'South America', 1800000, 32, 2.1, 480, 42.1, 28.8, 0.69, 14, 2015, 'World Bank / FAO', 'https://worldbank.org', FALSE),
('BR', 'Brazil', 'South America', 1500000, 28, 2.5, 490, 48.5, 29.2, 0.71, 13, 2020, 'World Bank / FAO', 'https://worldbank.org', FALSE);

-- Insert projected data (future scenarios) - SSP2-4.5 (middle of the road)
INSERT INTO geo_impacts (
    country_code, country_name, region,
    deforestation_rate_hectares_per_year, aqi_avg, water_stress_index,
    co2_emissions_mt_per_year, renewable_energy_percent, protected_land_percent,
    biodiversity_intactness_index, plastic_waste_mt_per_year,
    data_year, source_name, source_url, is_projected, projection_scenario, confidence_level
) VALUES 
-- US Projections
('US', 'United States', 'North America', 250000, 35, 3.8, 4200, 35.0, 15.0, 0.68, 45, 2030, 'IPCC AR6 / IEA', 'https://ipcc.ch', TRUE, 'SSP2-4.5', 0.75),
('US', 'United States', 'North America', 220000, 32, 4.2, 3500, 45.0, 18.0, 0.70, 48, 2040, 'IPCC AR6 / IEA', 'https://ipcc.ch', TRUE, 'SSP2-4.5', 0.65),
('US', 'United States', 'North America', 200000, 30, 4.5, 2800, 55.0, 20.0, 0.72, 50, 2050, 'IPCC AR6 / IEA', 'https://ipcc.ch', TRUE, 'SSP2-4.5', 0.55),

-- CN Projections
('CN', 'China', 'East Asia', 150000, 50, 5.2, 9500, 40.0, 22.0, 0.58, 48, 2030, 'IPCC AR6 / IEA', 'https://ipcc.ch', TRUE, 'SSP2-4.5', 0.75),
('CN', 'China', 'East Asia', 120000, 45, 5.5, 7800, 52.0, 25.0, 0.62, 45, 2040, 'IPCC AR6 / IEA', 'https://ipcc.ch', TRUE, 'SSP2-4.5', 0.65),
('CN', 'China', 'East Asia', 100000, 40, 5.8, 5500, 65.0, 28.0, 0.66, 42, 2050, 'IPCC AR6 / IEA', 'https://ipcc.ch', TRUE, 'SSP2-4.5', 0.55),

-- BR Projections
('BR', 'Brazil', 'South America', 1200000, 25, 3.0, 400, 55.0, 32.0, 0.75, 10, 2030, 'IPCC AR6 / IEA', 'https://ipcc.ch', TRUE, 'SSP2-4.5', 0.75),
('BR', 'Brazil', 'South America', 900000, 22, 3.5, 320, 65.0, 35.0, 0.78, 9, 2040, 'IPCC AR6 / IEA', 'https://ipcc.ch', TRUE, 'SSP2-4.5', 0.65),
('BR', 'Brazil', 'South America', 700000, 20, 4.0, 250, 75.0, 38.0, 0.80, 8, 2050, 'IPCC AR6 / IEA', 'https://ipcc.ch', TRUE, 'SSP2-4.5', 0.55);
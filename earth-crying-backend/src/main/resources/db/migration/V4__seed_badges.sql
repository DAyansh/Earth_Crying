-- Flyway Migration: V4__seed_badges.sql
-- Badge definitions for gamified habit change

INSERT INTO badges (id, code, name, description, icon_url, category, requirement_type, requirement_value, rarity, is_active, created_at) VALUES
('00000000-0000-0000-0000-000000000001', 'FIRST_CONVERSATION', 'First Conversation', 'You asked your first question about environmental impact', 'https://example.com/badges/conversation.png', 'engagement', 'action_count', 1, 'COMMON', true, CURRENT_TIMESTAMP),
('00000000-0000-0000-0000-000000000002', 'TREE_FRIEND', 'Tree Friend', 'Learned about digital carbon footprint through our impact quiz', 'https://example.com/badges/tree-friend.png', 'knowledge', 'quiz_completion', 1, 'COMMON', true, CURRENT_TIMESTAMP),
('00000000-0000-0000-0000-000000000003', 'STREAK_STARTER', 'Streak Starter', 'Completed your first pledge', 'https://example.com/badges/streak.png', 'habit', 'streak_count', 1, 'COMMON', true, CURRENT_TIMESTAMP),
('00000000-0000-0000-0000-000000000004', 'SEVEN_DAYS', '7-Day Streak', 'Maintained a pledge for 7 consecutive days', 'https://example.com/badges/7-days.png', 'habit', 'streak_count', 7, 'COMMON', true, CURRENT_TIMESTAMP),
('00000000-0000-0000-0000-000000000005', 'THIRTY_DAYS', '30-Day Streak', 'Maintained a pledge for 30 consecutive days', 'https://example.com/badges/30-days.png', 'habit', 'streak_count', 30, 'RARE', true, CURRENT_TIMESTAMP),
('00000000-0000-0000-0000-000000000006', 'CLIMATE_CHAMPION', 'Climate Champion', 'Completed actions in 5 different impact categories', 'https://example.com/badges/champion.png', 'engagement', 'categories_completed', 5, 'EPIC', true, CURRENT_TIMESTAMP),
('00000000-0000-0000-0000-000000000007', 'WATER_WARRIOR', 'Water Warrior', 'Took action to reduce water footprint', 'https://example.com/badges/water-warrior.png', 'action', 'impact_category', 1, 'RARE', true, CURRENT_TIMESTAMP),
('00000000-0000-0000-0000-000000000008', 'WASTE_WARRIOR', 'Waste Warrior', 'Took action to reduce e-waste', 'https://example.com/badges/waste-warrior.png', 'action', 'impact_category', 1, 'RARE', true, CURRENT_TIMESTAMP),
('00000000-0000-0000-0000-000000000009', 'COMMUNITY_HERO', 'Community Hero', 'Submitted 3 community solutions', 'https://example.com/badges/community-hero.png', 'contributions', 'solution_count', 3, 'EPIC', true, CURRENT_TIMESTAMP),
('00000000-0000-0000-0000-000000000010', 'EARTH_AMBASSADOR', 'Earth Ambassador', 'Shared positive environmental news with 10 friends', 'https://example.com/badges/ambassador.png', 'sharing', 'shares_count', 10, 'EPIC', true, CURRENT_TIMESTAMP);
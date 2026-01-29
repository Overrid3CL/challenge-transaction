-- ==========================================
-- Migration V1: Create table BUSINESS_CATEGORY
-- ==========================================
-- Description: Catalog of business categories
-- Author: Alvarado
-- Date: 2026-01-27
-- ==========================================

CREATE TABLE business_category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Comments
COMMENT ON TABLE business_category IS 'Catalog of business categories or industry sectors';
COMMENT ON COLUMN business_category.id IS 'Unique identifier for the category';
COMMENT ON COLUMN business_category.name IS 'Category name (e.g., Retail, Food Service)';
COMMENT ON COLUMN business_category.description IS 'Detailed description of the category';
COMMENT ON COLUMN business_category.created_at IS 'Timestamp when the record was created';
-- ==========================================
-- Migration V3: Create table BUSINESS
-- ==========================================
-- Description: Catalog of businesses where transactions occur
-- Author: Ariel Alvarado
-- Date: 2026-01-27
-- ==========================================

CREATE TABLE business (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category_id INTEGER NOT NULL,
    address VARCHAR(200),
    deleted BOOLEAN DEFAULT false NOT NULL,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    -- Foreign Keys
    CONSTRAINT fk_business_category FOREIGN KEY (category_id) 
        REFERENCES business_category(id) 
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- Comments
COMMENT ON TABLE business IS 'Catalog of businesses or commercial establishments';
COMMENT ON COLUMN business.id IS 'Unique identifier for the business';
COMMENT ON COLUMN business.name IS 'Name of the business or establishment';
COMMENT ON COLUMN business.category_id IS 'Reference to the business category';
COMMENT ON COLUMN business.address IS 'Physical address of the business';
COMMENT ON COLUMN business.deleted IS 'Soft delete: indicates if the business is logically deleted';
COMMENT ON COLUMN business.deleted_at IS 'Timestamp when the record was soft deleted';
COMMENT ON COLUMN business.created_at IS 'Timestamp when the record was created';
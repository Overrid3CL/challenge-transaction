-- ==========================================
-- Migration V5: Create Indexes
-- ==========================================
-- Description: Indexes to optimize frequent queries
-- Author: Ariel Alvarado
-- Date: 2026-01-27
-- ==========================================

-- ==========================================
-- INDEXES: USER
-- ==========================================

-- Index for email search (already UNIQUE, but explicit for documentation)
CREATE INDEX idx_user_email ON "user"(email);

-- Partial index for active users (most queried)
CREATE INDEX idx_user_active ON "user"(id) 
    WHERE deleted = false;

-- Index for search by user type
CREATE INDEX idx_user_type ON "user"(user_type) 
    WHERE deleted = false;

-- ==========================================
-- INDEXES: BUSINESS
-- ==========================================

-- Index for JOIN with category
CREATE INDEX idx_business_category ON business(category_id) 
    WHERE deleted = false;

-- Partial index for active businesses
CREATE INDEX idx_business_active ON business(id) 
    WHERE deleted = false;

-- Index for search by name
CREATE INDEX idx_business_name ON business(name) 
    WHERE deleted = false;

-- ==========================================
-- INDEXES: TRANSACTION (Most important)
-- ==========================================

-- Partial index for active transactions (most queried)
CREATE INDEX idx_transaction_active ON transaction(id) 
    WHERE deleted = false;

-- Indexes for search by user
CREATE INDEX idx_transaction_user ON transaction(user_id) 
    WHERE deleted = false;

-- Index for search/sort by date
CREATE INDEX idx_transaction_date ON transaction(transaction_date DESC) 
    WHERE deleted = false;

-- Index for search by business
CREATE INDEX idx_transaction_business ON transaction(business_id) 
    WHERE deleted = false;

-- Composite indexes for common queries

-- Transactions by user ordered by date (very frequent query)
CREATE INDEX idx_transaction_user_date 
    ON transaction(user_id, transaction_date DESC) 
    WHERE deleted = false;


-- Transactions by business ordered by date
CREATE INDEX idx_transaction_business_date 
    ON transaction(business_id, transaction_date DESC) 
    WHERE deleted = false;

-- Index for reports by date range
CREATE INDEX idx_transaction_date_range 
    ON transaction(transaction_date) 
    WHERE deleted = false;

-- Index for searching deleted transactions (audit)
CREATE INDEX idx_transaction_deleted 
    ON transaction(deleted_at) 
    WHERE deleted = true;

-- ==========================================
-- COMMENTS
-- ==========================================

COMMENT ON INDEX idx_user_active IS 'Partial index for active users (not deleted)';
COMMENT ON INDEX idx_transaction_user_date IS 'Composite index for queries of transactions by user ordered by date';
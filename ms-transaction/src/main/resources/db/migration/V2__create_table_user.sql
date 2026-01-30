-- ==========================================
-- Migration V2: Create table USER
-- ==========================================
-- Description: System users (users and administrators)
-- Author: Ariel Alvarado
-- Date: 2026-01-27
-- ==========================================

CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    user_type VARCHAR(20) DEFAULT 'USER' NOT NULL,
    deleted BOOLEAN DEFAULT false NOT NULL,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    -- Constraints
    CONSTRAINT chk_user_type CHECK (user_type IN ('USER', 'ADMIN')),
    CONSTRAINT chk_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Comments
COMMENT ON TABLE "user" IS 'System users who can send or receive transactions';
COMMENT ON COLUMN "user".id IS 'Unique identifier for the user';
COMMENT ON COLUMN "user".name IS 'Full name of the user';
COMMENT ON COLUMN "user".email IS 'Unique email address of the user';
COMMENT ON COLUMN "user".phone IS 'Contact phone number';
COMMENT ON COLUMN "user".user_type IS 'Type of user: USER or ADMIN';
COMMENT ON COLUMN "user".deleted IS 'Soft delete: indicates if the user is logically deleted';
COMMENT ON COLUMN "user".deleted_at IS 'Timestamp when the record was soft deleted';
COMMENT ON COLUMN "user".created_at IS 'Timestamp when the record was created';
COMMENT ON COLUMN "user".updated_at IS 'Timestamp when the record was last updated';
-- ==========================================
-- Migration V4: Create table TRANSACTION
-- ==========================================
-- Description: Record of transactions between users
-- Author: Ariel Alvarado
-- Date: 2026-01-27 
-- ==========================================

CREATE TABLE transaction (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    business_id INTEGER NOT NULL,
    amount INTEGER NOT NULL,
    transaction_date TIMESTAMP WITH TIME ZONE NOT NULL,
    description TEXT,
    deleted BOOLEAN DEFAULT false NOT NULL,
    deleted_at TIMESTAMP,
    deleted_by INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    -- Foreign Keys
    CONSTRAINT fk_transaction_user FOREIGN KEY (user_id) 
        REFERENCES "user"(id) 
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
        
        
    CONSTRAINT fk_transaction_business FOREIGN KEY (business_id) 
        REFERENCES business(id) 
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
        
    CONSTRAINT fk_transaction_deleted_by FOREIGN KEY (deleted_by) 
        REFERENCES "user"(id) 
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    
    -- Business Rules Constraints
    CONSTRAINT chk_amount_positive CHECK (amount > 0),
    CONSTRAINT chk_date_not_future CHECK (transaction_date <= CURRENT_TIMESTAMP)
);

-- Comments
COMMENT ON TABLE transaction IS 'Record of financial transactions between users';
COMMENT ON COLUMN transaction.id IS 'Unique identifier for the transaction';
COMMENT ON COLUMN transaction.user_id IS 'User who sends/initiates the transaction';
COMMENT ON COLUMN transaction.business_id IS 'Business where the transaction took place';
COMMENT ON COLUMN transaction.amount IS 'Transaction amount in Chilean pesos (must be positive)';
COMMENT ON COLUMN transaction.transaction_date IS 'Date and time of the transaction (cannot be in the future)';
COMMENT ON COLUMN transaction.description IS 'Additional description or note about the transaction';
COMMENT ON COLUMN transaction.deleted IS 'Soft delete: indicates if the transaction is logically deleted';
COMMENT ON COLUMN transaction.deleted_at IS 'Timestamp when the record was soft deleted';
COMMENT ON COLUMN transaction.deleted_by IS 'User who performed the deletion';
COMMENT ON COLUMN transaction.created_at IS 'Timestamp when the record was created';
COMMENT ON COLUMN transaction.updated_at IS 'Timestamp when the record was last updated';
-- ==========================================
-- Migration V9: Create table TRANSACTION_AUDIT_LOG
-- ==========================================
-- Description: Audit log for transaction events (CREATE, DELETE)
-- Author: Ariel Alvarado
-- Date: 2026-01-27
-- ==========================================

CREATE TABLE transaction_audit_log (
    id SERIAL PRIMARY KEY,
    transaction_id INTEGER NOT NULL,
    action VARCHAR(10) NOT NULL,
    performed_by INTEGER,
    performed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    metadata JSONB,

    CONSTRAINT fk_audit_transaction FOREIGN KEY (transaction_id)
        REFERENCES transaction(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_audit_performed_by FOREIGN KEY (performed_by)
        REFERENCES "user"(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    CONSTRAINT chk_audit_action CHECK (action IN ('CREATE', 'UPDATE', 'DELETE'))
);

-- Indexes for frequent queries
CREATE INDEX idx_audit_transaction ON transaction_audit_log(transaction_id);
CREATE INDEX idx_audit_performed_at ON transaction_audit_log(performed_at DESC);
CREATE INDEX idx_audit_performed_by ON transaction_audit_log(performed_by) WHERE performed_by IS NOT NULL;
CREATE INDEX idx_audit_action ON transaction_audit_log(action);

-- Comments
COMMENT ON TABLE transaction_audit_log IS 'Audit log of who performed CREATE, UPDATE or DELETE on transactions';
COMMENT ON COLUMN transaction_audit_log.id IS 'Unique identifier for the audit entry';
COMMENT ON COLUMN transaction_audit_log.transaction_id IS 'Transaction that was affected';
COMMENT ON COLUMN transaction_audit_log.action IS 'Event type: CREATE, UPDATE or DELETE';
COMMENT ON COLUMN transaction_audit_log.performed_by IS 'User who performed the action (NULL if system/job)';
COMMENT ON COLUMN transaction_audit_log.performed_at IS 'When the action was performed';
COMMENT ON COLUMN transaction_audit_log.metadata IS 'Optional extra context (e.g. changed fields, IP, user agent)';

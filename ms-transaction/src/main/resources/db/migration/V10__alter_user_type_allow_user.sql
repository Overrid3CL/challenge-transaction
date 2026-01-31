-- ==========================================
-- Migration V10: Allow user_type TENPISTA in table user
-- ==========================================
-- Description: Extend chk_user_type to allow TENPISTA (in addition to USER, ADMIN)
-- ==========================================

ALTER TABLE "user" DROP CONSTRAINT IF EXISTS chk_user_type;
ALTER TABLE "user" ADD CONSTRAINT chk_user_type CHECK (user_type IN ('TENPISTA', 'ADMIN', 'USER'));

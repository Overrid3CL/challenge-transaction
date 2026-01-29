-- ==========================================
-- Migration V8: Initial Data - USER
-- ==========================================
-- Description: Sample users for testing
-- Author: Ariel Alvarado
-- Date: 2026-01-27
-- ==========================================

-- Tenpista Users
INSERT INTO "user" (name, email, phone, user_type) VALUES
('Juan Perez Gonzalez', 'juan.perez@email.com', '+56912345678', 'TENPISTA'),
('Maria Gonzalez Silva', 'maria.gonzalez@email.com', '+56987654321', 'TENPISTA'),
('Pedro Silva Martinez', 'pedro.silva@email.com', '+56911111111', 'TENPISTA'),
('Ana Lopez Rojas', 'ana.lopez@email.com', '+56922222222', 'TENPISTA'),
('Carlos Ramirez Torres', 'carlos.ramirez@email.com', '+56933333333', 'TENPISTA'),
('Sofia Martinez Diaz', 'sofia.martinez@email.com', '+56944444444', 'TENPISTA'),
('Diego Fernandez Castro', 'diego.fernandez@email.com', '+56955555555', 'TENPISTA'),
('Valentina Rojas Munoz', 'valentina.rojas@email.com', '+56966666666', 'TENPISTA'),
('Sebastian Torres Vargas', 'sebastian.torres@email.com', '+56977777777', 'TENPISTA'),
('Isidora Castro Soto', 'isidora.castro@email.com', '+56988888888', 'TENPISTA');

-- Admin User
INSERT INTO "user" (name, email, phone, user_type) VALUES
('System Administrator', 'admin@challenge.cl', '+56900000000', 'ADMIN');

-- Sample Transactions
INSERT INTO transaction (user_id, business_id, amount, transaction_date, description) VALUES
(1, 1, 45000, '2025-01-20 10:30:00', 'Monthly supermarket purchase'),
(1, 5, 25000, '2025-01-21 14:15:00', 'Business lunch'),
(2, 9, 15000, '2025-01-22 09:00:00', 'Medications'),
(3, 12, 80000, '2025-01-23 16:45:00', 'Clothing purchase'),
(2, 16, 35000, '2025-01-24 08:20:00', 'Fuel tank'),
(4, 3, 120000, '2025-01-25 11:30:00', 'Large supermarket purchase'),
(5, 20, 55000, '2025-01-26 15:00:00', 'Gaming mouse and keyboard'),
(1, 6, 18000, '2025-01-27 12:00:00', 'Coffee and snacks');

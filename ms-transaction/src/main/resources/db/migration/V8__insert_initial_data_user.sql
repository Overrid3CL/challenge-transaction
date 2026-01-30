-- ==========================================
-- Migration V8: Initial Data - USER
-- ==========================================
-- Description: Sample users for testing
-- Author: Ariel Alvarado
-- Date: 2026-01-27
-- ==========================================

-- Users
INSERT INTO "user" (name, email, phone, user_type) VALUES
('Juan Perez Gonzalez', 'juan.perez@email.com', '+56912345678'  , 'USER'),  
('Maria Gonzalez Silva', 'maria.gonzalez@email.com', '+56987654321', 'USER'),
('Pedro Silva Martinez', 'pedro.silva@email.com', '+56911111111', 'USER'),
('Ana Lopez Rojas', 'ana.lopez@email.com', '+56922222222', 'USER'),
('Carlos Ramirez Torres', 'carlos.ramirez@email.com', '+56933333333', 'USER'),
('Sofia Martinez Diaz', 'sofia.martinez@email.com', '+56944444444', 'USER'),
('Diego Fernandez Castro', 'diego.fernandez@email.com', '+56955555555', 'USER'),
('Valentina Rojas Munoz', 'valentina.rojas@email.com', '+56966666666', 'USER'),
('Sebastian Torres Vargas', 'sebastian.torres@email.com', '+56977777777', 'USER'),
('Isidora Castro Soto', 'isidora.castro@email.com', '+56988888888', 'USER');

-- Admin User
INSERT INTO "user" (name, email, phone, user_type) VALUES
('System Administrator', 'admin@challenge.cl', '+56900000000', 'ADMIN');

INSERT INTO transaction (user_id, business_id, amount, transaction_date, description) VALUES
(1, 1, 45000, '2025-01-20 10:30:00', 'Compra mensual supermercado'),           -- Comercio: Jumbo
(1, 5, 25000, '2025-01-21 14:15:00', 'Compra semanal supermercado'),         -- Comercio: Tottus
(2, 9, 15000, '2025-01-22 09:00:00', 'Pizza y bebidas delivery'),            -- Alimentos y Bebidas: Domino's
(3, 12, 80000, '2025-01-23 16:45:00', 'Café y snacks'),                       -- Alimentos y Bebidas: Starbucks
(2, 16, 35000, '2025-01-24 08:20:00', 'Medicamentos en farmacia'),            -- Salud: Dr. Simi
(4, 3, 120000, '2025-01-25 11:30:00', 'Compra grande supermercado'),          -- Comercio: Santa Isabel
(5, 20, 55000, '2025-01-26 15:00:00', 'Ropa y accesorios'),                   -- Moda: Zara (business_id 20)
(1, 6, 18000, '2025-01-27 12:00:00', 'Almuerzo en restaurante');               -- Alimentos y Bebidas: Central Restaurant

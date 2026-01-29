-- ==========================================
-- Migration V7: Initial Data - BUSINESS
-- ==========================================
-- Description: Initial catalog of businesses
-- Author: Ariel Alvarado
-- Date: 2026-01-27 
-- ==========================================

-- Retail (category_id = 1)
INSERT INTO business (name, category_id, address) VALUES
('Jumbo', 1, 'Av. Libertador Bernardo O''Higgins 3250, Santiago'),
('Lider', 1, 'Av. Américo Vespucio 1501, La Florida'),
('Santa Isabel', 1, 'Av. Grecia 8735, Peñalolén'),
('Unimarc', 1, 'Av. Vicuña Mackenna 6100, La Florida'),
('Tottus', 1, 'Av. Departamental 1233, Santiago');

-- Food Service (category_id = 2)
INSERT INTO business (name, category_id, address) VALUES
('Central Restaurant', 2, 'Compañía 1054, Santiago Centro'),
('Havanna Café', 2, 'Av. Providencia 2124, Providencia'),
('Subway', 2, 'Mall Plaza Vespucio'),
('Domino''s Pizza', 2, 'Av. Apoquindo 4900, Las Condes'),
('McDonald''s', 2, 'Av. Vicuña Mackenna 7110, La Florida'),
('KFC', 2, 'Av. Grecia 9570, Peñalolén'),
('Starbucks', 2, 'Costanera Center, Providencia');

-- Health (category_id = 3)
INSERT INTO business (name, category_id, address) VALUES
('Cruz Verde Pharmacy', 3, 'Av. Providencia 1760, Providencia'),
('Salcobrand', 3, 'Av. Grecia 9630, Peñalolén'),
('Ahumada Pharmacy', 3, 'Alameda 3120, Santiago Centro'),
('Dr. Simi', 3, 'Av. Vicuña Mackenna 6890, La Florida');

-- Fashion (category_id = 4)
INSERT INTO business (name, category_id, address) VALUES
('Falabella', 4, 'Av. Libertador Bernardo O''Higgins 949, Santiago'),
('Ripley', 4, 'Av. Grecia 9727, Peñalolén'),
('Paris', 4, 'Av. Providencia 2507, Providencia'),
('Zara', 4, 'Costanera Center, Providencia'),
('H&M', 4, 'Mall Plaza Vespucio, La Florida');

-- Construction (category_id = 5)
INSERT INTO business (name, category_id, address) VALUES
('Sodimac', 5, 'Av. Américo Vespucio 1501, La Florida'),
('Easy', 5, 'Av. Vicuña Mackenna 6100, La Florida'),
('Homecenter', 5, 'Av. Grecia 9727, Peñalolén');

-- Education (category_id = 6)
INSERT INTO business (name, category_id, address) VALUES
('National Bookstore', 6, 'Alameda 2850, Santiago'),
('Antartica', 6, 'Av. Providencia 2124, Providencia'),
('Chilean Book Fair', 6, 'Huérfanos 623, Santiago');

-- Fuel (category_id = 7)
INSERT INTO business (name, category_id, address) VALUES
('Copec', 7, 'Av. Vicuña Mackenna 7500, La Florida'),
('Shell', 7, 'Av. Grecia 10100, Peñalolén'),
('Petrobras', 7, 'Av. Américo Vespucio 1800, La Florida'),
('Terpel', 7, 'Av. Las Condes 12000, Las Condes');

-- Electronics (category_id = 8)
INSERT INTO business (name, category_id, address) VALUES
('PC Factory', 8, 'Av. Providencia 2330, Providencia'),
('Hites', 8, 'Av. Grecia 9630, Peñalolén'),
('Abcdin', 8, 'Av. Vicuña Mackenna 6850, La Florida'),
('Lider Electro', 8, 'Mall Plaza Vespucio, La Florida');

-- Entertainment (category_id = 9)
INSERT INTO business (name, category_id, address) VALUES
('Cinemark', 9, 'Mall Plaza Vespucio, La Florida'),
('Cine Hoyts', 9, 'Costanera Center, Providencia'),
('Municipal Theater', 9, 'Agustinas 794, Santiago');

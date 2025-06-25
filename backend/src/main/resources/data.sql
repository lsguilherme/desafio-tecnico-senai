-- SQL para inserir 15 produtos, 3 cupons e 8 aplicações de cupons no banco de dados
-- Coloque este script em src/main/resources/data.sql para ser executado na inicialização do Spring Boot.

-- IMPORTANTE:
-- As IDs dos produtos e cupons serão geradas automaticamente se a coluna 'id' for auto_increment.
-- Os campos 'created_at', 'updated_at' serão preenchidos com o timestamp atual.
-- 'final_price' será calculado dinamicamente pelo backend com base nos cupons aplicados.
-- As colunas de desconto direto (discount_value, discount_percent, etc.) foram removidas dos INSERTs de products
-- pois sua ProductEntity as deriva de ProductCouponApplicationEntity e CouponEntity.

-- 1. Inserir Produtos (15 produtos)
INSERT INTO products (name, description, stock, price, created_at, updated_at, deleted_at) VALUES
-- Produtos sem desconto inicial
('Notebook Essencial', 'Notebook para estudos e trabalho, rápido e leve.', 50, 2500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Monitor Gamer 24"', 'Monitor com alta taxa de atualização para jogos imersivos.', 30, 1200.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Teclado Mecânico RGB', 'Teclado com switches de alta performance e iluminação RGB.', 100, 350.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Mouse Ergonômico Wireless', 'Mouse sem fio com design ergonômico para longas horas de uso.', 70, 150.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Webcam Full HD', 'Webcam de alta definição para reuniões e streamings.', 40, 200.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Roteador Wi-Fi 6', 'Roteador de última geração para internet de alta velocidade.', 25, 450.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Impressora Multifuncional', 'Impressora a jato de tinta com scanner e copiadora.', 15, 600.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Headset Gamer PRO', 'Headset com áudio espacial e microfone de alta qualidade.', 60, 480.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),

-- Produtos que receberão desconto via cupom
('Smartphone Ultra Max', 'Smartphone top de linha com câmera profissional e bateria duradoura.', 20, 4000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Smart TV 55" 4K', 'Televisor inteligente com resolução 4K e acesso a streaming.', 10, 3000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Fone de Ouvido Bluetooth', 'Fone de ouvido sem fio com cancelamento de ruído.', 80, 250.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Câmera Mirrorless', 'Câmera digital avançada para fotografia e vídeo.', 5, 5000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Tablet Pro 11"', 'Tablet potente para produtividade e entretenimento.', 18, 2000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Smartwatch Fitness', 'Relógio inteligente com monitor de batimentos e GPS.', 60, 400.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Console de Última Geração', 'Console de videogame para os jogos mais recentes.', 8, 3500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);


-- 2. Inserir Cupons (3 cupons)
-- valid_until usa DATEADD para compatibilidade com H2
INSERT INTO coupons (code, type, discount_value, one_shot, max_uses, valid_from, valid_until, created_at, updated_at, deleted_at) VALUES
('DESC10', 'PERCENT', 10.00, FALSE, NULL, CURRENT_TIMESTAMP, DATEADD('YEAR', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL), -- 10% de desconto
('FIXO50', 'FIXED', 50.00, FALSE, NULL, CURRENT_TIMESTAMP, DATEADD('YEAR', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL), -- R$50 de desconto
('SUPER20', 'PERCENT', 20.00, FALSE, NULL, CURRENT_TIMESTAMP, DATEADD('YEAR', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL); -- 20% de desconto

-- 3. Aplicar Cupons a Produtos (8 aplicações de cupons)
-- Colunas 'created_at', 'updated_at' removidas, pois ProductCouponApplicationEntity não herda de AbstractBaseEntity
INSERT INTO product_coupon_applications (product_id, coupon_id, applied_at, removed_at) VALUES
-- Produtos com DESC10 (10% OFF)
((SELECT id FROM products WHERE name = 'Smartphone Ultra Max'), (SELECT id FROM coupons WHERE code = 'DESC10'), CURRENT_TIMESTAMP, NULL),
((SELECT id FROM products WHERE name = 'Smart TV 55" 4K'), (SELECT id FROM coupons WHERE code = 'DESC10'), CURRENT_TIMESTAMP, NULL),
((SELECT id FROM products WHERE name = 'Fone de Ouvido Bluetooth'), (SELECT id FROM coupons WHERE code = 'DESC10'), CURRENT_TIMESTAMP, NULL),

-- Produtos com FIXO50 (R$50 OFF)
((SELECT id FROM products WHERE name = 'Tablet Pro 11"'), (SELECT id FROM coupons WHERE code = 'FIXO50'), CURRENT_TIMESTAMP, NULL),
((SELECT id FROM products WHERE name = 'Smartwatch Fitness'), (SELECT id FROM coupons WHERE code = 'FIXO50'), CURRENT_TIMESTAMP, NULL),

-- Produtos com SUPER20 (20% OFF)
((SELECT id FROM products WHERE name = 'Console de Última Geração'), (SELECT id FROM coupons WHERE code = 'SUPER20'), CURRENT_TIMESTAMP, NULL),
((SELECT id FROM products WHERE name = 'Câmera Mirrorless'), (SELECT id FROM coupons WHERE code = 'SUPER20'), CURRENT_TIMESTAMP, NULL),
((SELECT id FROM products WHERE name = 'Headset Gamer PRO'), (SELECT id FROM coupons WHERE code = 'SUPER20'), CURRENT_TIMESTAMP, NULL);
INSERT INTO products (name, description, stock, price, created_at, updated_at, deleted_at) VALUES
('Notebook Essencial', 'Notebook para estudos e trabalho, rápido e leve.', 50, 2500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Monitor Gamer 24"', 'Monitor com alta taxa de atualização para jogos imersivos.', 30, 1200.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Teclado Mecânico RGB', 'Teclado com switches de alta performance e iluminação RGB.', 100, 350.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Mouse Ergonômico Wireless', 'Mouse sem fio com design ergonômico para longas horas de uso.', 70, 150.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Webcam Full HD', 'Webcam de alta definição para reuniões e streamings.', 40, 200.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Roteador Wi-Fi 6', 'Roteador de última geração para internet de alta velocidade.', 25, 450.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Impressora Multifuncional', 'Impressora a jato de tinta com scanner e copiadora.', 15, 600.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Headset Gamer PRO', 'Headset com áudio espacial e microfone de alta qualidade.', 60, 480.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),

('Smartphone Ultra Max', 'Smartphone top de linha com câmera profissional e bateria duradoura.', 20, 4000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Smart TV 55" 4K', 'Televisor inteligente com resolução 4K e acesso a streaming.', 10, 3000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Fone de Ouvido Bluetooth', 'Fone de ouvido sem fio com cancelamento de ruído.', 80, 250.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Câmera Mirrorless', 'Câmera digital avançada para fotografia e vídeo.', 5, 5000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Tablet Pro 11"', 'Tablet potente para produtividade e entretenimento.', 18, 2000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Smartwatch Fitness', 'Relógio inteligente com monitor de batimentos e GPS.', 60, 400.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Console de Última Geração', 'Console de videogame para os jogos mais recentes.', 8, 3500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO coupons (code, type, discount_value, one_shot, max_uses, valid_from, valid_until, created_at, updated_at, deleted_at) VALUES
('DESC10', 'PERCENT', 10.00, FALSE, NULL, CURRENT_TIMESTAMP, DATEADD('YEAR', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('FIXO50', 'FIXED', 50.00, FALSE, NULL, CURRENT_TIMESTAMP, DATEADD('YEAR', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('SUPER20', 'PERCENT', 20.00, FALSE, NULL, CURRENT_TIMESTAMP, DATEADD('YEAR', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO product_coupon_applications (product_id, coupon_id, applied_at, removed_at) VALUES
((SELECT id FROM products WHERE name = 'Smartphone Ultra Max'), (SELECT id FROM coupons WHERE code = 'DESC10'), CURRENT_TIMESTAMP, NULL),
((SELECT id FROM products WHERE name = 'Smart TV 55" 4K'), (SELECT id FROM coupons WHERE code = 'DESC10'), CURRENT_TIMESTAMP, NULL),
((SELECT id FROM products WHERE name = 'Fone de Ouvido Bluetooth'), (SELECT id FROM coupons WHERE code = 'DESC10'), CURRENT_TIMESTAMP, NULL),

((SELECT id FROM products WHERE name = 'Tablet Pro 11"'), (SELECT id FROM coupons WHERE code = 'FIXO50'), CURRENT_TIMESTAMP, NULL),
((SELECT id FROM products WHERE name = 'Smartwatch Fitness'), (SELECT id FROM coupons WHERE code = 'FIXO50'), CURRENT_TIMESTAMP, NULL),

((SELECT id FROM products WHERE name = 'Console de Última Geração'), (SELECT id FROM coupons WHERE code = 'SUPER20'), CURRENT_TIMESTAMP, NULL),
((SELECT id FROM products WHERE name = 'Câmera Mirrorless'), (SELECT id FROM coupons WHERE code = 'SUPER20'), CURRENT_TIMESTAMP, NULL),
((SELECT id FROM products WHERE name = 'Headset Gamer PRO'), (SELECT id FROM coupons WHERE code = 'SUPER20'), CURRENT_TIMESTAMP, NULL);
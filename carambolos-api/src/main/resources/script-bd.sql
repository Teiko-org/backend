-- =====================================================
-- SCRIPT COMPLETO DO BANCO DE DADOS TEIKO - VERSÃO COM IMAGENS
-- =====================================================
/*!40101 SET NAMES utf8mb4 */;

CREATE DATABASE IF NOT EXISTS teiko DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE teiko;

-- -----------------------------------------------------
-- Table teiko.usuario
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.usuario (
  id INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(60) NOT NULL,
  senha VARCHAR(60) NOT NULL,
  contato VARCHAR(14) NOT NULL,
  data_nascimento DATE NULL,
  genero VARCHAR(20) NULL,
  imagem_url VARCHAR(500) NULL,
  sys_admin TINYINT NULL,
  is_ativo TINYINT NULL,
  PRIMARY KEY (id),
  INDEX nome_idx (nome ASC) VISIBLE,
  INDEX contato_idx (contato ASC) VISIBLE
);

-- -----------------------------------------------------
-- Table teiko.jwt_token_blacklist
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.jwt_token_blacklist (
  id INT NOT NULL AUTO_INCREMENT,
  token VARCHAR(500) NOT NULL,
  blacklisted_at DATETIME NOT NULL,
  PRIMARY KEY (id),
  INDEX token_idx (token ASC) VISIBLE
);

-- -----------------------------------------------------
-- Table teiko.carrinho
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.carrinho (
  id INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  itens TEXT NULL,
  data_ultima_atualizacao DATETIME NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_carrinho_usuario (usuario_id),
  INDEX fk_carrinho_usuario_idx (usuario_id ASC) VISIBLE,
  CONSTRAINT fk_carrinho_usuario
    FOREIGN KEY (usuario_id)
    REFERENCES teiko.usuario (id)
    ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table teiko.endereco
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.endereco (
  id INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(20) NULL,
  cep VARCHAR(128) NOT NULL,
  estado VARCHAR(256) NOT NULL,
  cidade VARCHAR(256) NOT NULL,
  bairro VARCHAR(256) NOT NULL,
  logradouro VARCHAR(256) NOT NULL,
  numero VARCHAR(128) NOT NULL,
  complemento VARCHAR(256) NULL,
  referencia VARCHAR(256) NULL,
  usuario_id INT NULL,
  is_ativo TINYINT NULL,
  dedup_hash VARCHAR(64) NULL,
  PRIMARY KEY (id),
  INDEX fk_endereco_usuario1_idx (usuario_id ASC) VISIBLE,
  INDEX cep_idx (cep ASC) VISIBLE,
  INDEX dedup_hash_idx (dedup_hash ASC) VISIBLE,
  CONSTRAINT fk_endereco_usuario1
    FOREIGN KEY (usuario_id)
    REFERENCES teiko.usuario (id)
);

-- -----------------------------------------------------
-- Table teiko.produto_fornada
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.produto_fornada (
  id INT NOT NULL AUTO_INCREMENT,
  produto VARCHAR(50) NULL,
  descricao VARCHAR(70) NULL,
  valor DOUBLE NULL,
  categoria VARCHAR(70),
  is_ativo TINYINT NULL,
  PRIMARY KEY (id),
  INDEX produto_fornada_idx (produto ASC) VISIBLE
);

-- -----------------------------------------------------
-- Table teiko.imagem_produto_fornada
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.imagem_produto_fornada (
    id INT NOT NULL AUTO_INCREMENT,
    produto_fornada_id INT NOT NULL,
    url VARCHAR(500) NOT NULL,
    PRIMARY KEY (id),
    INDEX produto_fornada_idx (produto_fornada_id ASC),
    CONSTRAINT fk_imagem_produto_fornada FOREIGN KEY (produto_fornada_id)
    REFERENCES teiko.produto_fornada (id)
);

-- -----------------------------------------------------
-- Table teiko.fornada
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.fornada (
  id INT NOT NULL AUTO_INCREMENT,
  data_inicio DATE NULL,
  data_fim DATE NULL,
  is_ativo TINYINT NULL,
  PRIMARY KEY (id),
  INDEX data_inicio_idx (data_inicio ASC) VISIBLE,
  INDEX data_fim_idx (data_fim ASC) VISIBLE
);

-- -----------------------------------------------------
-- Table teiko.fornada_da_vez
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.fornada_da_vez (
  id INT NOT NULL AUTO_INCREMENT,
  produto_fornada_id INT NOT NULL,
  fornada_id INT NOT NULL,
  quantidade INT NULL,
  is_ativo TINYINT NULL,
  PRIMARY KEY (id, produto_fornada_id, fornada_id),
  INDEX fornada_idx (fornada_id ASC) VISIBLE,
  INDEX produto_fornada_idx (produto_fornada_id ASC) VISIBLE,
  CONSTRAINT fk_produto_fornada
    FOREIGN KEY (produto_fornada_id)
    REFERENCES teiko.produto_fornada (id),
  CONSTRAINT fk_fornada
    FOREIGN KEY (fornada_id)
    REFERENCES teiko.fornada (id)
);

-- -----------------------------------------------------
-- Table teiko.pedido_fornada
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.pedido_fornada (
  id INT NOT NULL AUTO_INCREMENT,
  fornada_da_vez_id INT NOT NULL,
  endereco_id INT NULL,
  usuario_id INT NULL,
  quantidade INT NOT NULL,
  data_previsao_entrega DATE NOT NULL,
  is_ativo TINYINT NULL,
  tipo_entrega VARCHAR(15) NOT NULL DEFAULT 'ENTREGA',
  nome_cliente VARCHAR(256) NOT NULL,
  telefone_cliente VARCHAR(256) NOT NULL,
  horario_retirada VARCHAR(10) NULL,
  observacoes VARCHAR(500) NULL,
  PRIMARY KEY (id),
  INDEX endereco1_idx (endereco_id ASC) VISIBLE,
  INDEX usuario1_idx (usuario_id ASC) VISIBLE,
  INDEX fornada_da_vez1_idx (fornada_da_vez_id ASC) VISIBLE,
  CONSTRAINT fk_pedido_fornada_endereco1
    FOREIGN KEY (endereco_id)
    REFERENCES teiko.endereco (id),
  CONSTRAINT fk_pedido_fornada_usuario1
    FOREIGN KEY (usuario_id)
    REFERENCES teiko.usuario (id),
  CONSTRAINT fk_pedido_fornada_fornada_da_vez1
    FOREIGN KEY (fornada_da_vez_id)
    REFERENCES teiko.fornada_da_vez (id)
);

-- -----------------------------------------------------
-- Table teiko.massa
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.massa (
  id INT NOT NULL AUTO_INCREMENT,
  sabor VARCHAR(255) NOT NULL,
  valor DOUBLE NOT NULL,
  is_ativo TINYINT NULL,
  PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table teiko.cobertura
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.cobertura (
  id INT NOT NULL AUTO_INCREMENT,
  cor VARCHAR(20) NOT NULL,
  descricao VARCHAR(70) NULL,
  is_ativo TINYINT NULL,
  PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table teiko.decoracao
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.decoracao (
  id INT NOT NULL AUTO_INCREMENT,
  observacao VARCHAR(70),
  nome VARCHAR(70),
  categoria VARCHAR(70),
  is_ativo TINYINT NULL,
  PRIMARY KEY (id)
);

-- Alias para compatibilidade com queries antigas que referenciam "decoracaoEntity"
-- (mantém os mesmos campos de teiko.decoracao)
CREATE TABLE IF NOT EXISTS teiko.decoracaoEntity LIKE teiko.decoracao;
INSERT IGNORE INTO teiko.decoracaoEntity SELECT * FROM teiko.decoracao;

-- -----------------------------------------------------
-- Table teiko.imagem_decoracao
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.imagem_decoracao (
    id INT NOT NULL AUTO_INCREMENT,
    decoracao_id INT NOT NULL,
    url VARCHAR(500) NOT NULL,
    PRIMARY KEY (id),
    INDEX decoracao_idx (decoracao_id ASC),
    CONSTRAINT fk_imagem_decoracao_decoracao FOREIGN KEY (decoracao_id)
    REFERENCES teiko.decoracao (id)
);

CREATE TABLE IF NOT EXISTS teiko.adicional (
	id INT NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(90),
    is_ativo TINYINT NULL,
	PRIMARY KEY (id),
    INDEX adicional_idx (id ASC)
);

CREATE TABLE IF NOT EXISTS teiko.adicional_decoracao (
	id INT NOT NULL AUTO_INCREMENT,
	decoracao_id INT NOT NULL,
    adicional_id INT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_decoracao_id_ad
		FOREIGN KEY (decoracao_id)
        REFERENCES teiko.decoracao (id),
	CONSTRAINT fk_adicional_id_ad
		FOREIGN KEY (adicional_id)
        REFERENCES teiko.adicional(id)
);

-- -----------------------------------------------------
-- Table teiko.recheio_unitario
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.recheio_unitario (
  id INT NOT NULL AUTO_INCREMENT,
  sabor VARCHAR(255) NOT NULL,
  descricao VARCHAR(255) NULL,
  valor DOUBLE NOT NULL,
  is_ativo TINYINT NULL,
  PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table teiko.recheio_exclusivo
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.recheio_exclusivo (
  id INT NOT NULL AUTO_INCREMENT,
  recheio_unitario_id1 INT NOT NULL,
  recheio_unitario_id2 INT NOT NULL,
  nome VARCHAR(255) NOT NULL,
  is_ativo TINYINT NULL,
  PRIMARY KEY (id, recheio_unitario_id1, recheio_unitario_id2),
  INDEX recheio_unitario1_idx (recheio_unitario_id1 ASC) VISIBLE,
  INDEX recheio_unitario2_idx (recheio_unitario_id2 ASC) VISIBLE,
  CONSTRAINT fk_recheio_exclusivo_recheio_unitario1
    FOREIGN KEY (recheio_unitario_id1)
    REFERENCES teiko.recheio_unitario (id),
  CONSTRAINT fk_recheio_exclusivo_recheio_unitario2
    FOREIGN KEY (recheio_unitario_id2)
    REFERENCES teiko.recheio_unitario (id)
);

-- -----------------------------------------------------
-- Table teiko.recheio_pedido
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.recheio_pedido (
  id INT NOT NULL AUTO_INCREMENT,
  recheio_unitario_id1 INT NULL,
  recheio_unitario_id2 INT NULL,
  recheio_exclusivo INT NULL,
  is_ativo TINYINT NULL,
  PRIMARY KEY (id),
  INDEX exclusivo1_idx (recheio_exclusivo ASC) VISIBLE,
  CONSTRAINT fk_unitario1
    FOREIGN KEY (recheio_unitario_id1)
    REFERENCES teiko.recheio_unitario (id),
  CONSTRAINT fk_unitario2
    FOREIGN KEY (recheio_unitario_id2)
    REFERENCES teiko.recheio_unitario (id),
  CONSTRAINT fk_recheio_exclusivo1
    FOREIGN KEY (recheio_exclusivo)
    REFERENCES teiko.recheio_exclusivo (id)
);

-- -----------------------------------------------------
-- Table teiko.bolo
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.bolo (
  id INT NOT NULL AUTO_INCREMENT,
  recheio_pedido_id INT NOT NULL,
  massa_id INT NOT NULL,
  cobertura_id INT NOT NULL,
  decoracao_id INT NULL,
  formato VARCHAR(45) NULL,
  tamanho VARCHAR(45) NULL,
  categoria VARCHAR(60),
  is_ativo TINYINT NULL,
  PRIMARY KEY (id, recheio_pedido_id, massa_id, cobertura_id),
  INDEX fk_Bolo_massa1_idx (massa_id ASC) VISIBLE,
  INDEX fk_Bolo_decoracao1_idx (decoracao_id ASC) VISIBLE,
  INDEX fk_Bolo_cobertura1_idx (cobertura_id ASC) VISIBLE,
  CONSTRAINT fk_Bolo_recheio_pedido1
    FOREIGN KEY (recheio_pedido_id)
    REFERENCES teiko.recheio_pedido (id),
  CONSTRAINT fk_Bolo_massa1
    FOREIGN KEY (massa_id)
    REFERENCES teiko.massa (id),
  CONSTRAINT fk_Bolo_decoracao1
    FOREIGN KEY (decoracao_id)
    REFERENCES teiko.decoracao (id),
  CONSTRAINT fk_Bolo_cobertura1
    FOREIGN KEY (cobertura_id)
    REFERENCES teiko.cobertura (id)
);

-- -----------------------------------------------------
-- Table teiko.pedido_bolo
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.pedido_bolo (
  id INT NOT NULL AUTO_INCREMENT,
  endereco_id INT NULL,
  bolo_id INT NOT NULL,
  usuario_id INT NULL,
  observacao VARCHAR(70) NULL,
  data_previsao_entrega DATE NOT NULL,
  data_ultima_atualizacao DATETIME NOT NULL,
  tipo_entrega VARCHAR(15) NOT NULL DEFAULT 'ENTREGA',
  nome_cliente VARCHAR(256) NOT NULL,
  telefone_cliente VARCHAR(256) NOT NULL,
  horario_retirada VARCHAR(10) NULL,
  is_ativo TINYINT NULL,
  PRIMARY KEY (id),
  INDEX fk_pedido_bolo_usuario1_idx (usuario_id ASC) VISIBLE,
  INDEX fk_pedido_bolo_endereco1_idx (endereco_id ASC) VISIBLE,
  INDEX fk_pedido_bolo_Bolo1_idx (bolo_id ASC) VISIBLE,
  CONSTRAINT fk_pedido_bolo_usuario1
    FOREIGN KEY (usuario_id)
    REFERENCES teiko.usuario (id),
  CONSTRAINT fk_pedido_bolo_endereco1
    FOREIGN KEY (endereco_id)
    REFERENCES teiko.endereco (id),
  CONSTRAINT fk_pedido_bolo_Bolo1
    FOREIGN KEY (bolo_id)
    REFERENCES teiko.bolo (id)
);

-- REMOVIDO: sistema de imagens usa decoracao/imagem_decoracao

-- -----------------------------------------------------
-- Table teiko.resumo_pedido
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS teiko.resumo_pedido (
  id INT NOT NULL AUTO_INCREMENT,
  status VARCHAR(45) NOT NULL,
  valor DOUBLE NOT NULL,
  data_pedido DATETIME NOT NULL,
  data_entrega DATETIME NULL,
  pedido_fornada_id INT NULL,
  pedido_bolo_id INT NULL,
  is_ativo TINYINT NULL,
  PRIMARY KEY (id),
  INDEX fk_fornada1_idx (pedido_fornada_id ASC) VISIBLE,
  INDEX fk_pedido_bolo1_idx (pedido_bolo_id ASC) VISIBLE,
  CONSTRAINT fk_resumo_pedido_pedido_fornada1
    FOREIGN KEY (pedido_fornada_id)
    REFERENCES teiko.pedido_fornada (id),
  CONSTRAINT fk_resumo_pedido_pedido_bolo1
    FOREIGN KEY (pedido_bolo_id)
    REFERENCES teiko.pedido_bolo (id)
);


-- =====================================================
-- DADOS INICIAIS (demonstracao / apresentacao)
-- Login admin: 11999999999 ou 5511912345671 | senha: 123456
-- Recriar banco no Docker: docker compose down -v && docker compose up -d mysql
--
-- Alinhado ao Assistant (Kuroko): principais clientes via get_recent_orders,
-- produtos via get_top_products (decoracoes), massas via get_pending_doughs,
-- entregas via get_upcoming_deliveries (datas proximas a "hoje" na demo: maio/2026).
-- Top clientes esperados: Joao Souza, Fernanda Lima, Carla Mendes, Ana Oliveira, Ricardo Alves.
-- Grafico Massas/Recheios por mes: pedidos 2023-2025 com volume variado por mes (bloco historico + extras dashboard).
-- =====================================================

INSERT INTO teiko.usuario (id, nome, senha, contato, data_nascimento, sys_admin, is_ativo) VALUES
(1, 'Admin Sistema', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '11999999999', '1990-01-15', 1, 1),
(2, 'Murilo Admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '5511912345671', '1998-06-20', 1, 1),
(3, 'Equipe Carambolos', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '11988880001', '1995-03-10', 1, 1),
(4, 'João Souza', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '11987654321', '1988-11-02', 0, 1),
(5, 'Ana Oliveira', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '11912345678', '1992-07-18', 0, 1),
(6, 'Carla Mendes', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '11998765432', '1990-09-25', 0, 1),
(7, 'Fernanda Lima', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '11976543210', '1994-04-12', 0, 1),
(8, 'Ricardo Alves', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '11965432109', '1985-12-30', 0, 1);

INSERT INTO teiko.endereco (id, nome, cep, estado, cidade, bairro, logradouro, numero, complemento, referencia, usuario_id, is_ativo) VALUES
(1, 'Casa', '01310100', 'SP', 'São Paulo', 'Bela Vista', 'Av. Paulista', '1578', 'Apto 42', 'Próximo ao MASP', 4, 1),
(2, 'Trabalho', '04543011', 'SP', 'São Paulo', 'Itaim Bibi', 'Rua Bandeira Paulista', '726', 'Sala 12', NULL, 4, 1),
(3, 'Casa', '05407002', 'SP', 'São Paulo', 'Pinheiros', 'Rua dos Pinheiros', '498', NULL, 'Portão azul', 5, 1),
(4, 'Casa', '04038001', 'SP', 'São Paulo', 'Vila Mariana', 'Rua Domingos de Morais', '2564', 'Bloco B', NULL, 6, 1),
(5, 'Casa', '05001000', 'SP', 'São Paulo', 'Perdizes', 'Rua Caiubi', '500', NULL, NULL, 7, 1),
(6, 'Casa', '01414001', 'SP', 'São Paulo', 'Jardins', 'Alameda Santos', '2400', 'Cobertura', NULL, 8, 1),
(7, 'Loja', '01310200', 'SP', 'São Paulo', 'Bela Vista', 'Rua Augusta', '1500', NULL, 'Retirada na loja', 2, 1),
(8, 'Centro', '01001000', 'SP', 'São Paulo', 'Sé', 'Praça da Sé', '100', NULL, NULL, 3, 1);

INSERT INTO teiko.massa (id, sabor, valor, is_ativo) VALUES
(1, 'cacau', 5.00, 1),
(2, 'cacau_expresso', 5.00, 1),
(3, 'baunilha', 5.00, 1),
(4, 'red_velvet', 5.00, 1);

INSERT INTO teiko.cobertura (id, cor, descricao, is_ativo) VALUES
(1, 'Branco', 'Cobertura cremosa de baunilha', 1),
(2, 'Preto', 'Cobertura de chocolate meio amargo', 1),
(3, 'Rosa', 'Cobertura de morango com brilho', 1),
(4, 'Dourado', 'Buttercream com tons caramelo', 1);

INSERT INTO teiko.decoracao (id, observacao, nome, categoria, is_ativo) VALUES
(1, 'Flores secas e fitas', 'Flores Silvestres', 'Vintage', 1),
(2, 'Flores naturais e folhagens', 'Jardim Floral', 'Floral', 1),
(3, 'Tema infantil colorido', 'Festa My Carambolo', 'My Carambolo', 1),
(4, 'Camadas texturizadas elegantes', 'Casamento Shag', 'Shag Cake', 1);

INSERT INTO teiko.decoracaoEntity SELECT * FROM teiko.decoracao;

INSERT INTO teiko.imagem_decoracao (decoracao_id, url) VALUES
(1, 'https://picsum.photos/seed/carambolo-vintage/400/400'),
(2, 'https://picsum.photos/seed/carambolo-floral/400/400'),
(3, 'https://picsum.photos/seed/carambolo-infantil/400/400'),
(4, 'https://images.unsplash.com/photo-1519225421980-715cb0215aed?w=400&h=400&fit=crop');

INSERT INTO teiko.adicional (id, descricao, is_ativo) VALUES
(1, 'Disco ball', 1),
(2, 'Desenho', 1),
(3, 'Pérolas na finalização', 1),
(4, 'Metalizado (Prata ou Dourado)', 1),
(5, 'Glitter', 1),
(6, 'Cereja (Com ou sem glitter)', 1),
(7, 'Laços', 1),
(8, 'Escrita', 1),
(9, 'Borda (Topo e Base)', 1),
(10, 'Lacinhos', 1);

INSERT INTO teiko.adicional_decoracao (decoracao_id, adicional_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8),
(2, 1), (2, 3), (2, 5), (2, 8), (2, 9),
(3, 9), (3, 10),
(4, 5);

INSERT INTO teiko.recheio_unitario (id, sabor, descricao, valor, is_ativo) VALUES
(1, 'creamcheese_frosting', 'Creamcheese Frosting', 10.00, 1),
(2, 'devil_s_cake_ganache_meio_amargo', 'Devil''s Cake (Ganache meio-amargo)', 10.00, 1),
(3, 'zanza_ganache_meio_amargo_e_reducao_de_frutas_vermelhas', 'Zanza (Ganache e frutas vermelhas)', 10.00, 1),
(4, 'brunna_brigadeiro_de_limao_siciliano', 'Brunna (Brigadeiro de limão siciliano)', 10.00, 1),
(5, 'marilia_brigadeiro_meio_amargo', 'Marilia (Brigadeiro meio-amargo)', 10.00, 1),
(6, 'hugo_brigadeiro_meio_amargo_e_brigadeiro_de_ninho', 'Hugo (Meio-amargo e ninho)', 10.00, 1),
(7, 'bia_benego_cocada_cremosa_de_coco_queimado', 'Bia Benego (Cocada cremosa)', 10.00, 1),
(8, 'duda_brigadeiro_de_doce_de_leite', 'Duda (Doce de leite)', 10.00, 1),
(9, 'giovanna_brigadeiro_de_pistache', 'Giovanna (Pistache)', 10.00, 1),
(10, 'juliana_creme_4_leites_e_reducao_de_frutas_vermelhas', 'Juliana (4 leites)', 10.00, 1);

INSERT INTO teiko.recheio_exclusivo (id, recheio_unitario_id1, recheio_unitario_id2, nome, is_ativo) VALUES
(1, 1, 2, 'Creamcheese com Devil''s Cake', 1),
(2, 1, 3, 'Creamcheese com Zanza', 1),
(3, 3, 2, 'Zanza com Devil''s Cake', 1);

INSERT INTO teiko.recheio_pedido (id, recheio_unitario_id1, recheio_unitario_id2, recheio_exclusivo, is_ativo) VALUES
(1, 1, 2, NULL, 1),
(2, NULL, NULL, 1, 1),
(3, 3, 4, NULL, 1),
(4, 5, 6, NULL, 1),
(5, 7, 8, NULL, 1),
(6, NULL, NULL, 2, 1),
(7, 9, 10, NULL, 1),
(8, 1, 3, NULL, 1),
(9, 4, 5, NULL, 1),
(10, 6, 7, NULL, 1),
(11, 8, 9, NULL, 1),
(12, NULL, NULL, 3, 1);

INSERT INTO teiko.bolo (id, recheio_pedido_id, massa_id, cobertura_id, decoracao_id, formato, tamanho, categoria, is_ativo) VALUES
(1, 1, 1, 1, 1, 'CIRCULO', 'TAMANHO_5', 'Carambolo', 1),
(2, 2, 2, 2, 4, 'CORACAO', 'TAMANHO_7', 'Casamento', 1),
(3, 3, 3, 3, 2, 'CIRCULO', 'TAMANHO_12', 'Aniversário', 1),
(4, 4, 4, 2, 3, 'CIRCULO', 'TAMANHO_15', 'Aniversário', 1),
(5, 5, 1, 4, 1, 'CIRCULO', 'TAMANHO_7', 'Carambolo', 1),
(6, 6, 2, 1, 4, 'CORACAO', 'TAMANHO_12', 'Casamento', 1),
(7, 7, 3, 2, 2, 'CIRCULO', 'TAMANHO_5', 'Carambolo', 1),
(8, 8, 4, 3, 3, 'CIRCULO', 'TAMANHO_17', 'Aniversário', 1),
(9, 9, 1, 2, 1, 'CIRCULO', 'TAMANHO_12', 'Carambolo', 1),
(10, 10, 2, 4, 2, 'CORACAO', 'TAMANHO_7', 'Casamento', 1),
(11, 11, 3, 1, 3, 'CIRCULO', 'TAMANHO_15', 'Aniversário', 1),
(12, 12, 4, 2, 4, 'CIRCULO', 'TAMANHO_12', 'Casamento', 1);

INSERT INTO teiko.produto_fornada (id, produto, descricao, valor, categoria, is_ativo) VALUES
(1, 'Pão de Queijo', 'Tradicional mineiro', 8.00, 'Salgados', 1),
(2, 'Croissant', 'Manteiga francesa', 14.00, 'Salgados', 1),
(3, 'Brownie de Cacau', 'Intenso com nozes', 18.00, 'Doces', 1),
(4, 'Cookie Chocolate', 'Duplo chocolate', 12.00, 'Doces', 1),
(5, 'Pão de Mel', 'Cobertura de chocolate', 10.00, 'Doces', 1),
(6, 'Banana Bread', 'Com canela', 16.00, 'Doces', 1),
(7, 'Muffin Mirtilo', 'Massa fofa', 11.00, 'Doces', 1),
(8, 'Quiche de Queijo', 'Almoço fornada', 22.00, 'Salgados', 1);

INSERT INTO teiko.imagem_produto_fornada (produto_fornada_id, url) VALUES
(1, 'https://picsum.photos/seed/fornada-pq/320/320'),
(2, 'https://picsum.photos/seed/fornada-croissant/320/320'),
(3, 'https://picsum.photos/seed/fornada-brownie/320/320'),
(4, 'https://picsum.photos/seed/fornada-cookie/320/320'),
(5, 'https://picsum.photos/seed/fornada-paomel/320/320'),
(6, 'https://picsum.photos/seed/fornada-banana/320/320'),
(7, 'https://picsum.photos/seed/fornada-muffin/320/320'),
(8, 'https://picsum.photos/seed/fornada-quiche/320/320');

INSERT INTO teiko.fornada (id, data_inicio, data_fim, is_ativo) VALUES
(1, '2026-03-03', '2026-03-09', 0),
(2, '2026-04-07', '2026-04-13', 0),
(3, '2026-05-05', '2026-05-18', 0),
(4, '2026-05-20', '2026-05-30', 1);

INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES
(1, 1, 4, 60, 1),
(2, 2, 4, 40, 1),
(3, 3, 4, 35, 1),
(4, 4, 4, 50, 1),
(5, 5, 4, 45, 1),
(6, 6, 4, 30, 1),
(7, 7, 4, 40, 1),
(8, 8, 4, 25, 1),
(9, 1, 3, 80, 1),
(10, 3, 3, 50, 1),
(11, 2, 2, 40, 1),
(12, 4, 1, 60, 1);

INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES
(1, 1, 1, 4, 12, '2026-05-22', 1, 'ENTREGA', 'João Souza', '11987654321', 'Entregar após 14h'),
(2, 1, 3, 5, 6, '2026-05-29', 1, 'RETIRADA', 'Ana Oliveira', '11912345678', 'Retirada fornada'),
(3, 4, NULL, NULL, 15, '2026-05-25', 1, 'ENTREGA', 'Patricia Nunes', '11933221100', 'Sem amendoim'),
(4, 2, 7, 2, 6, '2026-05-26', 1, 'RETIRADA', 'Murilo Admin', '5511912345671', 'Retirada na loja'),
(5, 1, 4, 6, 10, '2026-05-27', 1, 'ENTREGA', 'Carla Mendes', '11998765432', NULL),
(6, 7, 5, 7, 5, '2026-05-28', 1, 'ENTREGA', 'Fernanda Lima', '11976543210', 'Deixar na portaria'),
(7, 1, 1, 4, 8, '2026-05-29', 1, 'ENTREGA', 'João Souza', '11987654321', 'Pão de queijo extra'),
(8, 1, 5, 7, 6, '2026-05-30', 1, 'ENTREGA', 'Fernanda Lima', '11976543210', NULL);

INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES
(1, 1, 1, 4, 'Sem glitter', '2026-05-29', '2026-05-28 09:15:00', 'ENTREGA', 'João Souza', '11987654321', 1),
(2, NULL, 2, NULL, 'Casamento sábado', '2026-05-29', '2026-05-28 10:00:00', 'RETIRADA', 'Maria Oliveira', '11912345678', 1),
(3, 3, 3, 5, 'Topper personalizado', '2026-06-02', '2026-05-27 14:30:00', 'ENTREGA', 'Ana Oliveira', '11912345678', 1),
(4, 4, 4, 6, 'Aniversário 15 anos', '2026-06-05', '2026-05-26 11:20:00', 'ENTREGA', 'Carla Mendes', '11998765432', 1),
(5, 5, 3, 7, NULL, '2026-05-30', '2026-05-28 08:45:00', 'ENTREGA', 'Fernanda Lima', '11976543210', 1),
(6, NULL, 2, NULL, 'Retirada 10h', '2026-05-29', '2026-05-28 07:30:00', 'RETIRADA', 'Ricardo Alves', '11965432109', 1),
(7, 2, 7, 4, 'Escrita: Parabéns', '2026-06-01', '2026-05-27 16:00:00', 'ENTREGA', 'João Souza', '11987654321', 1),
(8, 6, 8, 8, 'Evento corporativo', '2026-06-08', '2026-05-25 13:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', 1),
(9, 1, 9, 4, NULL, '2026-05-27', '2026-05-24 10:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', 1),
(10, 3, 10, 5, 'Coração', '2026-05-26', '2026-05-23 15:30:00', 'RETIRADA', 'Ana Oliveira', '11912345678', 1),
(11, NULL, 3, NULL, 'Flores naturais', '2026-05-25', '2026-05-22 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', 1),
(12, 4, 12, 6, 'Shag cake', '2026-05-24', '2026-05-21 17:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', 1),
(13, 5, 1, 7, NULL, '2026-05-23', '2026-05-20 12:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', 1),
(14, NULL, 2, NULL, NULL, '2026-05-22', '2026-05-19 14:00:00', 'RETIRADA', 'Paulo Ribeiro', '11900998877', 1),
(15, 1, 3, 4, 'Cliente cancelou festa', '2026-05-21', '2026-05-18 10:00:00', 'ENTREGA', 'João Souza', '11987654321', 1),
(16, 3, 4, 5, NULL, '2026-05-20', '2026-05-17 11:00:00', 'ENTREGA', 'Helena Prado', '11988776655', 1),
(17, NULL, 3, NULL, 'Urgente', '2026-05-30', '2026-05-28 11:30:00', 'RETIRADA', 'Carla Mendes', '11998765432', 1),
(18, 6, 4, 8, NULL, '2026-05-31', '2026-05-28 12:00:00', 'ENTREGA', 'Isabela Rocha', '11966554433', 1),
(19, 2, 7, 4, NULL, '2026-06-03', '2026-05-27 09:00:00', 'ENTREGA', 'João Souza', '11987654321', 1),
(20, 4, 8, 6, 'Entrega manhã', '2026-06-04', '2026-05-26 08:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', 1),
(21, 5, 9, 7, NULL, '2026-05-28', '2026-05-25 16:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', 1),
(22, NULL, 4, NULL, 'Retirada loja', '2026-05-27', '2026-05-24 13:00:00', 'RETIRADA', 'Carla Mendes', '11998765432', 1),
(23, 1, 1, 4, 'Laços dourados', '2026-05-26', '2026-05-23 10:00:00', 'ENTREGA', 'João Souza', '11987654321', 1),
(24, 3, 12, 5, NULL, '2026-05-25', '2026-05-22 14:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', 1),
(25, NULL, 1, NULL, NULL, '2026-05-24', '2026-05-21 09:00:00', 'RETIRADA', 'Camila Duarte', '11944332200', 1),
(26, 6, 2, 8, 'Casamento', '2026-05-23', '2026-05-20 15:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', 1),
(27, 4, 3, 6, NULL, '2026-05-22', '2026-05-19 11:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', 1),
(28, 5, 4, 7, 'Dupla camada recheio', '2026-05-21', '2026-05-18 16:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', 1),
(29, NULL, 5, NULL, 'Cancelado pelo cliente', '2026-05-20', '2026-05-17 12:00:00', 'RETIRADA', 'Daniel Pereira', '11933221144', 1),
(30, 1, 1, 4, NULL, '2026-05-19', '2026-05-16 10:00:00', 'ENTREGA', 'João Souza', '11987654321', 1),
(31, 3, 3, 5, NULL, '2026-05-18', '2026-05-15 14:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', 1),
(32, NULL, 8, NULL, 'Corporativo', '2026-05-17', '2026-05-14 09:00:00', 'RETIRADA', 'Marina Gomes', '11922113344', 1);

INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
(1, 'PENDENTE', 280.00, '2026-05-28 09:15:00', '2026-05-29 14:00:00', NULL, 1, 1),
(2, 'PENDENTE', 420.00, '2026-05-28 10:00:00', '2026-05-31 10:00:00', NULL, 2, 1),
(3, 'PENDENTE', 580.00, '2026-05-27 14:30:00', '2026-06-02 16:00:00', NULL, 3, 1),
(4, 'PENDENTE', 720.00, '2026-05-26 11:20:00', '2026-06-05 15:00:00', NULL, 4, 1),
(5, 'PENDENTE', 350.00, '2026-05-28 08:45:00', '2026-05-30 11:00:00', NULL, 5, 1),
(6, 'PENDENTE', 480.00, '2026-05-28 07:30:00', '2026-05-29 10:00:00', NULL, 6, 1),
(7, 'PENDENTE', 310.00, '2026-05-27 16:00:00', '2026-06-01 14:00:00', NULL, 7, 1),
(8, 'PENDENTE', 390.00, '2026-05-28 11:30:00', '2026-05-30 09:00:00', NULL, 17, 1),
(9, 'PENDENTE', 450.00, '2026-05-28 12:00:00', '2026-05-31 12:00:00', NULL, 18, 1),
(10, 'PAGO', 96.00, '2026-05-26 08:00:00', '2026-05-22 14:00:00', 1, NULL, 1),
(11, 'PAGO', 48.00, '2026-05-27 11:00:00', '2026-05-29 16:00:00', 2, NULL, 1),
(12, 'PAGO', 520.00, '2026-05-25 16:00:00', '2026-05-28 10:00:00', NULL, 21, 1),
(13, 'PAGO', 410.00, '2026-05-24 13:00:00', '2026-05-27 11:00:00', NULL, 22, 1),
(14, 'PAGO', 680.00, '2026-05-26 08:00:00', '2026-06-04 09:00:00', NULL, 20, 1),
(15, 'PAGO', 295.00, '2026-05-27 09:00:00', '2026-06-03 14:00:00', NULL, 19, 1),
(16, 'PAGO', 180.00, '2026-05-18 10:00:00', '2026-05-21 12:00:00', NULL, 15, 1),
(17, 'PAGO', 84.00, '2026-05-24 15:00:00', '2026-05-26 12:00:00', 4, NULL, 1),
(18, 'CONCLUIDO', 890.00, '2026-05-25 13:00:00', '2026-06-08 18:00:00', NULL, 8, 1),
(19, 'CONCLUIDO', 340.00, '2026-05-24 10:00:00', '2026-05-27 14:00:00', NULL, 9, 1),
(20, 'CONCLUIDO', 395.00, '2026-05-23 15:30:00', '2026-05-26 10:00:00', NULL, 10, 1),
(21, 'CONCLUIDO', 610.00, '2026-05-22 09:00:00', '2026-05-25 16:00:00', NULL, 11, 1),
(22, 'CONCLUIDO', 750.00, '2026-05-21 17:00:00', '2026-05-24 15:00:00', NULL, 12, 1),
(23, 'CONCLUIDO', 265.00, '2026-05-20 12:00:00', '2026-05-23 11:00:00', NULL, 13, 1),
(24, 'CONCLUIDO', 430.00, '2026-05-19 14:00:00', '2026-05-22 10:00:00', NULL, 14, 1),
(25, 'CONCLUIDO', 470.00, '2026-05-23 10:00:00', '2026-05-26 14:00:00', NULL, 23, 1),
(26, 'CONCLUIDO', 690.00, '2026-05-22 14:00:00', '2026-05-25 17:00:00', NULL, 24, 1),
(27, 'CONCLUIDO', 110.00, '2026-05-27 09:00:00', '2026-05-28 13:00:00', 5, NULL, 1),
(28, 'CONCLUIDO', 55.00, '2026-05-26 10:00:00', '2026-05-27 18:00:00', 6, NULL, 1),
(29, 'CONCLUIDO', 275.00, '2026-05-21 09:00:00', '2026-05-24 12:00:00', NULL, 25, 1),
(30, 'CONCLUIDO', 510.00, '2026-05-20 15:00:00', '2026-05-23 14:00:00', NULL, 26, 1),
(31, 'CONCLUIDO', 360.00, '2026-05-19 11:00:00', '2026-05-22 16:00:00', NULL, 27, 1),
(32, 'CONCLUIDO', 320.00, '2026-05-15 14:00:00', '2026-05-18 11:00:00', NULL, 31, 1),
(33, 'CONCLUIDO', 440.00, '2026-05-14 09:00:00', '2026-05-17 10:00:00', NULL, 32, 1),
(34, 'CONCLUIDO', 180.00, '2026-04-12 10:00:00', '2026-04-15 14:00:00', NULL, 16, 1),
(35, 'CONCLUIDO', 240.00, '2026-04-08 11:00:00', '2026-04-11 16:00:00', NULL, 30, 1),
(36, 'CANCELADO', 620.00, '2026-05-18 10:00:00', NULL, NULL, 15, 1),
(37, 'CANCELADO', 380.00, '2026-05-17 12:00:00', NULL, NULL, 29, 1),
(38, 'CANCELADO', 195.00, '2026-05-16 09:00:00', NULL, 3, NULL, 1),
(39, 'CANCELADO', 530.00, '2026-05-19 11:00:00', NULL, NULL, 28, 1),
(40, 'PAGO', 165.00, '2026-03-05 14:00:00', '2026-03-08 12:00:00', 1, NULL, 1),
(41, 'PENDENTE', 64.00, '2026-05-28 14:00:00', '2026-05-29 14:00:00', 7, NULL, 1),
(42, 'PAGO', 48.00, '2026-05-27 10:00:00', '2026-05-30 11:00:00', 8, NULL, 1);


-- Historico 2023-2025 (grafico Massas/Recheios por mes, fornadas e KPIs)

INSERT INTO teiko.fornada (id, data_inicio, data_fim, is_ativo) VALUES (5, '2023-01-05', '2023-03-20', 0);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (13, 1, 5, 50, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (9, 13, 3, 5, 10, '2023-03-15', 1, 'ENTREGA', 'Ana Oliveira', '11912345678', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (43, 'CONCLUIDO', 80.00, '2023-03-10 11:00:00', '2023-03-15 14:00:00', 9, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (14, 3, 5, 30, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (10, 14, 4, 6, 6, '2023-03-15', 1, 'ENTREGA', 'Carla Mendes', '11998765432', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (44, 'CONCLUIDO', 108.00, '2023-03-10 11:00:00', '2023-03-15 14:00:00', 10, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (15, 4, 5, 40, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (11, 15, 5, 7, 7, '2023-03-15', 1, 'ENTREGA', 'Fernanda Lima', '11976543210', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (45, 'CONCLUIDO', 84.00, '2023-03-10 11:00:00', '2023-03-15 14:00:00', 11, NULL, 1);
INSERT INTO teiko.fornada (id, data_inicio, data_fim, is_ativo) VALUES (6, '2023-04-05', '2023-06-20', 0);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (16, 1, 6, 50, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (12, 16, 6, 8, 8, '2023-06-15', 1, 'ENTREGA', 'Ricardo Alves', '11965432109', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (46, 'CONCLUIDO', 64.00, '2023-06-10 11:00:00', '2023-06-15 14:00:00', 12, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (17, 3, 6, 30, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (13, 17, 1, 4, 9, '2023-06-15', 1, 'ENTREGA', 'João Souza', '11987654321', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (47, 'CONCLUIDO', 162.00, '2023-06-10 11:00:00', '2023-06-15 14:00:00', 13, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (18, 4, 6, 40, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (14, 18, 3, 5, 10, '2023-06-15', 1, 'ENTREGA', 'Ana Oliveira', '11912345678', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (48, 'CONCLUIDO', 120.00, '2023-06-10 11:00:00', '2023-06-15 14:00:00', 14, NULL, 1);
INSERT INTO teiko.fornada (id, data_inicio, data_fim, is_ativo) VALUES (7, '2023-07-05', '2023-09-20', 0);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (19, 1, 7, 50, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (15, 19, 4, 6, 6, '2023-09-15', 1, 'ENTREGA', 'Carla Mendes', '11998765432', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (49, 'CONCLUIDO', 48.00, '2023-09-10 11:00:00', '2023-09-15 14:00:00', 15, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (20, 3, 7, 30, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (16, 20, 5, 7, 7, '2023-09-15', 1, 'ENTREGA', 'Fernanda Lima', '11976543210', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (50, 'CONCLUIDO', 126.00, '2023-09-10 11:00:00', '2023-09-15 14:00:00', 16, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (21, 4, 7, 40, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (17, 21, 6, 8, 8, '2023-09-15', 1, 'ENTREGA', 'Ricardo Alves', '11965432109', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (51, 'CONCLUIDO', 96.00, '2023-09-10 11:00:00', '2023-09-15 14:00:00', 17, NULL, 1);
INSERT INTO teiko.fornada (id, data_inicio, data_fim, is_ativo) VALUES (8, '2023-10-05', '2023-12-20', 0);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (22, 1, 8, 50, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (18, 22, 1, 4, 9, '2023-12-15', 1, 'ENTREGA', 'João Souza', '11987654321', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (52, 'CONCLUIDO', 72.00, '2023-12-10 11:00:00', '2023-12-15 14:00:00', 18, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (23, 3, 8, 30, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (19, 23, 3, 5, 10, '2023-12-15', 1, 'ENTREGA', 'Ana Oliveira', '11912345678', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (53, 'CONCLUIDO', 180.00, '2023-12-10 11:00:00', '2023-12-15 14:00:00', 19, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (24, 4, 8, 40, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (20, 24, 4, 6, 6, '2023-12-15', 1, 'ENTREGA', 'Carla Mendes', '11998765432', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (54, 'CONCLUIDO', 72.00, '2023-12-10 11:00:00', '2023-12-15 14:00:00', 20, NULL, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (33, 3, 1, 5, NULL, '2023-01-22', '2023-01-12 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (55, 'CONCLUIDO', 225.00, '2023-01-12 10:00:00', '2023-01-22 16:00:00', NULL, 33, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (34, 4, 2, 6, NULL, '2023-02-22', '2023-02-12 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (56, 'CONCLUIDO', 270.00, '2023-02-12 10:00:00', '2023-02-22 16:00:00', NULL, 34, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (35, 5, 3, 7, NULL, '2023-03-22', '2023-03-12 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (57, 'CONCLUIDO', 315.00, '2023-03-12 10:00:00', '2023-03-22 16:00:00', NULL, 35, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (36, 6, 4, 8, NULL, '2023-04-22', '2023-04-12 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (58, 'CONCLUIDO', 360.00, '2023-04-12 10:00:00', '2023-04-22 16:00:00', NULL, 36, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (37, 1, 1, 4, NULL, '2023-05-22', '2023-05-12 09:00:00', 'ENTREGA', 'João Souza', '11987654321', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (59, 'CONCLUIDO', 245.00, '2023-05-12 10:00:00', '2023-05-22 16:00:00', NULL, 37, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (38, 3, 2, 5, NULL, '2023-06-22', '2023-06-12 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (60, 'CONCLUIDO', 290.00, '2023-06-12 10:00:00', '2023-06-22 16:00:00', NULL, 38, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (39, 5, 3, 7, 'Pedido extra temporada', '2023-06-22', '2023-06-12 09:00:00', 'RETIRADA', 'Fernanda Lima', '11976543210', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (61, 'CONCLUIDO', 340.00, '2023-06-12 10:00:00', '2023-06-22 16:00:00', NULL, 39, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (40, 4, 3, 6, NULL, '2023-07-22', '2023-07-12 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (62, 'CONCLUIDO', 335.00, '2023-07-12 10:00:00', '2023-07-22 16:00:00', NULL, 40, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (41, 5, 4, 7, NULL, '2023-08-22', '2023-08-12 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (63, 'CONCLUIDO', 380.00, '2023-08-12 10:00:00', '2023-08-22 16:00:00', NULL, 41, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (42, 6, 1, 8, NULL, '2023-09-22', '2023-09-12 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (64, 'CONCLUIDO', 265.00, '2023-09-12 10:00:00', '2023-09-22 16:00:00', NULL, 42, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (43, 1, 2, 4, NULL, '2023-10-22', '2023-10-12 09:00:00', 'ENTREGA', 'João Souza', '11987654321', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (65, 'CONCLUIDO', 310.00, '2023-10-12 10:00:00', '2023-10-22 16:00:00', NULL, 43, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (44, 3, 3, 5, NULL, '2023-11-22', '2023-11-12 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (66, 'CONCLUIDO', 355.00, '2023-11-12 10:00:00', '2023-11-22 16:00:00', NULL, 44, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (45, 4, 4, 6, NULL, '2023-12-22', '2023-12-12 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (67, 'CONCLUIDO', 400.00, '2023-12-12 10:00:00', '2023-12-22 16:00:00', NULL, 45, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (46, 6, 1, 8, 'Pedido extra temporada', '2023-12-22', '2023-12-12 09:00:00', 'RETIRADA', 'Ricardo Alves', '11965432109', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (68, 'CONCLUIDO', 450.00, '2023-12-12 10:00:00', '2023-12-22 16:00:00', NULL, 46, 1);
INSERT INTO teiko.fornada (id, data_inicio, data_fim, is_ativo) VALUES (9, '2024-01-05', '2024-03-20', 0);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (25, 1, 9, 50, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (21, 25, 6, 8, 7, '2024-03-15', 1, 'ENTREGA', 'Ricardo Alves', '11965432109', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (69, 'CONCLUIDO', 56.00, '2024-03-10 11:00:00', '2024-03-15 14:00:00', 21, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (26, 3, 9, 30, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (22, 26, 1, 4, 8, '2024-03-15', 1, 'ENTREGA', 'João Souza', '11987654321', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (70, 'CONCLUIDO', 144.00, '2024-03-10 11:00:00', '2024-03-15 14:00:00', 22, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (27, 4, 9, 40, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (23, 27, 3, 5, 9, '2024-03-15', 1, 'ENTREGA', 'Ana Oliveira', '11912345678', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (71, 'CONCLUIDO', 108.00, '2024-03-10 11:00:00', '2024-03-15 14:00:00', 23, NULL, 1);
INSERT INTO teiko.fornada (id, data_inicio, data_fim, is_ativo) VALUES (10, '2024-04-05', '2024-06-20', 0);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (28, 1, 10, 50, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (24, 28, 4, 6, 10, '2024-06-15', 1, 'ENTREGA', 'Carla Mendes', '11998765432', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (72, 'CONCLUIDO', 80.00, '2024-06-10 11:00:00', '2024-06-15 14:00:00', 24, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (29, 3, 10, 30, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (25, 29, 5, 7, 6, '2024-06-15', 1, 'ENTREGA', 'Fernanda Lima', '11976543210', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (73, 'CONCLUIDO', 108.00, '2024-06-10 11:00:00', '2024-06-15 14:00:00', 25, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (30, 4, 10, 40, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (26, 30, 6, 8, 7, '2024-06-15', 1, 'ENTREGA', 'Ricardo Alves', '11965432109', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (74, 'CONCLUIDO', 84.00, '2024-06-10 11:00:00', '2024-06-15 14:00:00', 26, NULL, 1);
INSERT INTO teiko.fornada (id, data_inicio, data_fim, is_ativo) VALUES (11, '2024-07-05', '2024-09-20', 0);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (31, 1, 11, 50, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (27, 31, 1, 4, 8, '2024-09-15', 1, 'ENTREGA', 'João Souza', '11987654321', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (75, 'CONCLUIDO', 64.00, '2024-09-10 11:00:00', '2024-09-15 14:00:00', 27, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (32, 3, 11, 30, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (28, 32, 3, 5, 9, '2024-09-15', 1, 'ENTREGA', 'Ana Oliveira', '11912345678', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (76, 'CONCLUIDO', 162.00, '2024-09-10 11:00:00', '2024-09-15 14:00:00', 28, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (33, 4, 11, 40, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (29, 33, 4, 6, 10, '2024-09-15', 1, 'ENTREGA', 'Carla Mendes', '11998765432', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (77, 'CONCLUIDO', 120.00, '2024-09-10 11:00:00', '2024-09-15 14:00:00', 29, NULL, 1);
INSERT INTO teiko.fornada (id, data_inicio, data_fim, is_ativo) VALUES (12, '2024-10-05', '2024-12-20', 0);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (34, 1, 12, 50, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (30, 34, 5, 7, 6, '2024-12-15', 1, 'ENTREGA', 'Fernanda Lima', '11976543210', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (78, 'CONCLUIDO', 48.00, '2024-12-10 11:00:00', '2024-12-15 14:00:00', 30, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (35, 3, 12, 30, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (31, 35, 6, 8, 7, '2024-12-15', 1, 'ENTREGA', 'Ricardo Alves', '11965432109', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (79, 'CONCLUIDO', 126.00, '2024-12-10 11:00:00', '2024-12-15 14:00:00', 31, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (36, 4, 12, 40, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (32, 36, 1, 4, 8, '2024-12-15', 1, 'ENTREGA', 'João Souza', '11987654321', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (80, 'CONCLUIDO', 96.00, '2024-12-10 11:00:00', '2024-12-15 14:00:00', 32, NULL, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (47, 3, 1, 5, NULL, '2024-01-22', '2024-01-12 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (81, 'CONCLUIDO', 225.00, '2024-01-12 10:00:00', '2024-01-22 16:00:00', NULL, 47, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (48, 4, 2, 6, NULL, '2024-02-22', '2024-02-12 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (82, 'CONCLUIDO', 270.00, '2024-02-12 10:00:00', '2024-02-22 16:00:00', NULL, 48, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (49, 5, 3, 7, NULL, '2024-03-22', '2024-03-12 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (83, 'CONCLUIDO', 315.00, '2024-03-12 10:00:00', '2024-03-22 16:00:00', NULL, 49, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (50, 6, 4, 8, NULL, '2024-04-22', '2024-04-12 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (84, 'CONCLUIDO', 360.00, '2024-04-12 10:00:00', '2024-04-22 16:00:00', NULL, 50, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (51, 1, 1, 4, NULL, '2024-05-22', '2024-05-12 09:00:00', 'ENTREGA', 'João Souza', '11987654321', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (85, 'CONCLUIDO', 245.00, '2024-05-12 10:00:00', '2024-05-22 16:00:00', NULL, 51, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (52, 3, 2, 5, NULL, '2024-06-22', '2024-06-12 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (86, 'CONCLUIDO', 290.00, '2024-06-12 10:00:00', '2024-06-22 16:00:00', NULL, 52, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (53, 5, 3, 7, 'Pedido extra temporada', '2024-06-22', '2024-06-12 09:00:00', 'RETIRADA', 'Fernanda Lima', '11976543210', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (87, 'CONCLUIDO', 340.00, '2024-06-12 10:00:00', '2024-06-22 16:00:00', NULL, 53, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (54, 4, 3, 6, NULL, '2024-07-22', '2024-07-12 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (88, 'CONCLUIDO', 335.00, '2024-07-12 10:00:00', '2024-07-22 16:00:00', NULL, 54, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (55, 5, 4, 7, NULL, '2024-08-22', '2024-08-12 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (89, 'CONCLUIDO', 380.00, '2024-08-12 10:00:00', '2024-08-22 16:00:00', NULL, 55, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (56, 6, 1, 8, NULL, '2024-09-22', '2024-09-12 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (90, 'CONCLUIDO', 265.00, '2024-09-12 10:00:00', '2024-09-22 16:00:00', NULL, 56, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (57, 1, 2, 4, NULL, '2024-10-22', '2024-10-12 09:00:00', 'ENTREGA', 'João Souza', '11987654321', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (91, 'CONCLUIDO', 310.00, '2024-10-12 10:00:00', '2024-10-22 16:00:00', NULL, 57, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (58, 3, 3, 5, NULL, '2024-11-22', '2024-11-12 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (92, 'CONCLUIDO', 355.00, '2024-11-12 10:00:00', '2024-11-22 16:00:00', NULL, 58, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (59, 4, 4, 6, NULL, '2024-12-22', '2024-12-12 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (93, 'CONCLUIDO', 400.00, '2024-12-12 10:00:00', '2024-12-22 16:00:00', NULL, 59, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (60, 6, 1, 8, 'Pedido extra temporada', '2024-12-22', '2024-12-12 09:00:00', 'RETIRADA', 'Ricardo Alves', '11965432109', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (94, 'CONCLUIDO', 450.00, '2024-12-12 10:00:00', '2024-12-22 16:00:00', NULL, 60, 1);
INSERT INTO teiko.fornada (id, data_inicio, data_fim, is_ativo) VALUES (13, '2025-01-05', '2025-03-20', 0);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (37, 1, 13, 50, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (33, 37, 4, 6, 9, '2025-03-15', 1, 'ENTREGA', 'Carla Mendes', '11998765432', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (95, 'CONCLUIDO', 72.00, '2025-03-10 11:00:00', '2025-03-15 14:00:00', 33, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (38, 3, 13, 30, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (34, 38, 5, 7, 10, '2025-03-15', 1, 'ENTREGA', 'Fernanda Lima', '11976543210', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (96, 'CONCLUIDO', 180.00, '2025-03-10 11:00:00', '2025-03-15 14:00:00', 34, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (39, 4, 13, 40, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (35, 39, 6, 8, 6, '2025-03-15', 1, 'ENTREGA', 'Ricardo Alves', '11965432109', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (97, 'CONCLUIDO', 72.00, '2025-03-10 11:00:00', '2025-03-15 14:00:00', 35, NULL, 1);
INSERT INTO teiko.fornada (id, data_inicio, data_fim, is_ativo) VALUES (14, '2025-04-05', '2025-06-20', 0);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (40, 1, 14, 50, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (36, 40, 1, 4, 7, '2025-06-15', 1, 'ENTREGA', 'João Souza', '11987654321', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (98, 'CONCLUIDO', 56.00, '2025-06-10 11:00:00', '2025-06-15 14:00:00', 36, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (41, 3, 14, 30, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (37, 41, 3, 5, 8, '2025-06-15', 1, 'ENTREGA', 'Ana Oliveira', '11912345678', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (99, 'CONCLUIDO', 144.00, '2025-06-10 11:00:00', '2025-06-15 14:00:00', 37, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (42, 4, 14, 40, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (38, 42, 4, 6, 9, '2025-06-15', 1, 'ENTREGA', 'Carla Mendes', '11998765432', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (100, 'CONCLUIDO', 108.00, '2025-06-10 11:00:00', '2025-06-15 14:00:00', 38, NULL, 1);
INSERT INTO teiko.fornada (id, data_inicio, data_fim, is_ativo) VALUES (15, '2025-07-05', '2025-09-20', 0);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (43, 1, 15, 50, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (39, 43, 5, 7, 10, '2025-09-15', 1, 'ENTREGA', 'Fernanda Lima', '11976543210', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (101, 'CONCLUIDO', 80.00, '2025-09-10 11:00:00', '2025-09-15 14:00:00', 39, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (44, 3, 15, 30, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (40, 44, 6, 8, 6, '2025-09-15', 1, 'ENTREGA', 'Ricardo Alves', '11965432109', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (102, 'CONCLUIDO', 108.00, '2025-09-10 11:00:00', '2025-09-15 14:00:00', 40, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (45, 4, 15, 40, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (41, 45, 1, 4, 7, '2025-09-15', 1, 'ENTREGA', 'João Souza', '11987654321', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (103, 'CONCLUIDO', 84.00, '2025-09-10 11:00:00', '2025-09-15 14:00:00', 41, NULL, 1);
INSERT INTO teiko.fornada (id, data_inicio, data_fim, is_ativo) VALUES (16, '2025-10-05', '2025-12-20', 0);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (46, 1, 16, 50, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (42, 46, 3, 5, 8, '2025-12-15', 1, 'ENTREGA', 'Ana Oliveira', '11912345678', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (104, 'CONCLUIDO', 64.00, '2025-12-10 11:00:00', '2025-12-15 14:00:00', 42, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (47, 3, 16, 30, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (43, 47, 4, 6, 9, '2025-12-15', 1, 'ENTREGA', 'Carla Mendes', '11998765432', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (105, 'CONCLUIDO', 162.00, '2025-12-10 11:00:00', '2025-12-15 14:00:00', 43, NULL, 1);
INSERT INTO teiko.fornada_da_vez (id, produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES (48, 4, 16, 40, 1);
INSERT INTO teiko.pedido_fornada (id, fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, is_ativo, tipo_entrega, nome_cliente, telefone_cliente, observacoes) VALUES (44, 48, 5, 7, 10, '2025-12-15', 1, 'ENTREGA', 'Fernanda Lima', '11976543210', NULL);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (106, 'CONCLUIDO', 120.00, '2025-12-10 11:00:00', '2025-12-15 14:00:00', 44, NULL, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (61, 3, 1, 5, NULL, '2025-01-22', '2025-01-12 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (107, 'CONCLUIDO', 225.00, '2025-01-12 10:00:00', '2025-01-22 16:00:00', NULL, 61, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (62, 4, 2, 6, NULL, '2025-02-22', '2025-02-12 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (108, 'CONCLUIDO', 270.00, '2025-02-12 10:00:00', '2025-02-22 16:00:00', NULL, 62, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (63, 5, 3, 7, NULL, '2025-03-22', '2025-03-12 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (109, 'CONCLUIDO', 315.00, '2025-03-12 10:00:00', '2025-03-22 16:00:00', NULL, 63, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (64, 6, 4, 8, NULL, '2025-04-22', '2025-04-12 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (110, 'CONCLUIDO', 360.00, '2025-04-12 10:00:00', '2025-04-22 16:00:00', NULL, 64, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (65, 1, 1, 4, NULL, '2025-05-22', '2025-05-12 09:00:00', 'ENTREGA', 'João Souza', '11987654321', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (111, 'CONCLUIDO', 245.00, '2025-05-12 10:00:00', '2025-05-22 16:00:00', NULL, 65, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (66, 3, 2, 5, NULL, '2025-06-22', '2025-06-12 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (112, 'CONCLUIDO', 290.00, '2025-06-12 10:00:00', '2025-06-22 16:00:00', NULL, 66, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (67, 5, 3, 7, 'Pedido extra temporada', '2025-06-22', '2025-06-12 09:00:00', 'RETIRADA', 'Fernanda Lima', '11976543210', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (113, 'CONCLUIDO', 340.00, '2025-06-12 10:00:00', '2025-06-22 16:00:00', NULL, 67, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (68, 4, 3, 6, NULL, '2025-07-22', '2025-07-12 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (114, 'CONCLUIDO', 335.00, '2025-07-12 10:00:00', '2025-07-22 16:00:00', NULL, 68, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (69, 5, 4, 7, NULL, '2025-08-22', '2025-08-12 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (115, 'CONCLUIDO', 380.00, '2025-08-12 10:00:00', '2025-08-22 16:00:00', NULL, 69, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (70, 6, 1, 8, NULL, '2025-09-22', '2025-09-12 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (116, 'CONCLUIDO', 265.00, '2025-09-12 10:00:00', '2025-09-22 16:00:00', NULL, 70, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (71, 1, 2, 4, NULL, '2025-10-22', '2025-10-12 09:00:00', 'ENTREGA', 'João Souza', '11987654321', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (117, 'PAGO', 310.00, '2025-10-12 10:00:00', '2025-10-22 16:00:00', NULL, 71, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (72, 3, 3, 5, NULL, '2025-11-22', '2025-11-12 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (118, 'PAGO', 355.00, '2025-11-12 10:00:00', '2025-11-22 16:00:00', NULL, 72, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (73, 4, 4, 6, NULL, '2025-12-22', '2025-12-12 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (119, 'PAGO', 400.00, '2025-12-12 10:00:00', '2025-12-22 16:00:00', NULL, 73, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES (74, 6, 1, 8, 'Pedido extra temporada', '2025-12-22', '2025-12-12 09:00:00', 'RETIRADA', 'Ricardo Alves', '11965432109', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (120, 'PAGO', 450.00, '2025-12-12 10:00:00', '2025-12-22 16:00:00', NULL, 74, 1);


-- Pedidos extras 2023-2025: variedade de massas/recheios e volume por mes no dashboard

INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (75, 3, 1, 5, NULL, '2023-01-22', '2023-01-13 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (121, 'CONCLUIDO', 243.00, '2023-01-13 10:00:00', '2023-01-22 16:00:00', NULL, 75, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (76, 4, 1, 6, NULL, '2023-01-22', '2023-01-13 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (122, 'CONCLUIDO', 255.00, '2023-01-13 10:00:00', '2023-01-22 16:00:00', NULL, 76, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (77, 5, 3, 7, NULL, '2023-01-22', '2023-01-13 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (123, 'CONCLUIDO', 337.00, '2023-01-13 10:00:00', '2023-01-22 16:00:00', NULL, 77, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (78, 4, 2, 6, NULL, '2023-02-22', '2023-02-14 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (124, 'CONCLUIDO', 286.00, '2023-02-14 10:00:00', '2023-02-22 16:00:00', NULL, 78, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (79, 5, 2, 7, NULL, '2023-02-22', '2023-02-14 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (125, 'CONCLUIDO', 298.00, '2023-02-14 10:00:00', '2023-02-22 16:00:00', NULL, 79, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (80, 6, 4, 8, NULL, '2023-02-22', '2023-02-14 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (126, 'CONCLUIDO', 380.00, '2023-02-14 10:00:00', '2023-02-22 16:00:00', NULL, 80, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (81, 1, 1, 1, NULL, '2023-02-22', '2023-02-14 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (127, 'CONCLUIDO', 287.00, '2023-02-14 10:00:00', '2023-02-22 16:00:00', NULL, 81, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (82, 5, 3, 7, NULL, '2023-03-22', '2023-03-15 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (128, 'CONCLUIDO', 329.00, '2023-03-15 10:00:00', '2023-03-22 16:00:00', NULL, 82, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (83, 6, 3, 8, NULL, '2023-03-22', '2023-03-15 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (129, 'CONCLUIDO', 341.00, '2023-03-15 10:00:00', '2023-03-22 16:00:00', NULL, 83, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (84, 1, 1, 1, NULL, '2023-03-22', '2023-03-15 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (130, 'CONCLUIDO', 283.00, '2023-03-15 10:00:00', '2023-03-22 16:00:00', NULL, 84, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (85, 6, 4, 8, NULL, '2023-04-22', '2023-04-16 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (131, 'CONCLUIDO', 372.00, '2023-04-16 10:00:00', '2023-04-22 16:00:00', NULL, 85, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (86, 1, 4, 1, NULL, '2023-04-22', '2023-04-16 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (132, 'CONCLUIDO', 384.00, '2023-04-16 10:00:00', '2023-04-22 16:00:00', NULL, 86, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (87, 4, 2, 4, NULL, '2023-04-22', '2023-04-16 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (133, 'CONCLUIDO', 326.00, '2023-04-16 10:00:00', '2023-04-22 16:00:00', NULL, 87, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (88, 1, 3, 4, NULL, '2023-04-22', '2023-04-16 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (134, 'CONCLUIDO', 373.00, '2023-04-16 10:00:00', '2023-04-22 16:00:00', NULL, 88, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (89, 1, 1, 1, NULL, '2023-05-22', '2023-05-12 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (135, 'CONCLUIDO', 275.00, '2023-05-12 10:00:00', '2023-05-22 16:00:00', NULL, 89, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (90, 4, 1, 4, NULL, '2023-05-22', '2023-05-12 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (136, 'CONCLUIDO', 287.00, '2023-05-12 10:00:00', '2023-05-22 16:00:00', NULL, 90, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (91, 1, 4, 4, NULL, '2023-05-22', '2023-05-12 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (137, 'CONCLUIDO', 404.00, '2023-05-12 10:00:00', '2023-05-22 16:00:00', NULL, 91, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (92, 4, 2, 4, NULL, '2023-06-22', '2023-06-13 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (138, 'CONCLUIDO', 318.00, '2023-06-13 10:00:00', '2023-06-22 16:00:00', NULL, 92, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (93, 1, 2, 4, NULL, '2023-06-22', '2023-06-13 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (139, 'CONCLUIDO', 330.00, '2023-06-13 10:00:00', '2023-06-22 16:00:00', NULL, 93, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (94, 3, 2, 5, NULL, '2023-06-22', '2023-06-13 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (140, 'CONCLUIDO', 342.00, '2023-06-13 10:00:00', '2023-06-22 16:00:00', NULL, 94, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (95, 4, 2, 6, NULL, '2023-06-22', '2023-06-13 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (141, 'CONCLUIDO', 354.00, '2023-06-13 10:00:00', '2023-06-22 16:00:00', NULL, 95, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (96, 5, 1, 7, NULL, '2023-06-22', '2023-06-13 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (142, 'CONCLUIDO', 331.00, '2023-06-13 10:00:00', '2023-06-22 16:00:00', NULL, 96, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (97, 6, 3, 8, NULL, '2023-06-22', '2023-06-13 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (143, 'CONCLUIDO', 413.00, '2023-06-13 10:00:00', '2023-06-22 16:00:00', NULL, 97, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (98, NULL, 3, 4, NULL, '2023-07-22', '2023-07-14 09:00:00', 'RETIRADA', 'João Souza', '11987654321', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (144, 'CONCLUIDO', 361.00, '2023-07-14 10:00:00', '2023-07-22 16:00:00', NULL, 98, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (99, NULL, 3, 5, NULL, '2023-07-22', '2023-07-14 09:00:00', 'RETIRADA', 'Ana Oliveira', '11912345678', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (145, 'CONCLUIDO', 373.00, '2023-07-14 10:00:00', '2023-07-22 16:00:00', NULL, 99, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (100, 4, 2, 6, NULL, '2023-07-22', '2023-07-14 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (146, 'CONCLUIDO', 350.00, '2023-07-14 10:00:00', '2023-07-22 16:00:00', NULL, 100, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (101, 3, 4, 5, NULL, '2023-08-22', '2023-08-15 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (147, 'CONCLUIDO', 404.00, '2023-08-15 10:00:00', '2023-08-22 16:00:00', NULL, 101, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (102, 4, 4, 6, NULL, '2023-08-22', '2023-08-15 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (148, 'CONCLUIDO', 416.00, '2023-08-15 10:00:00', '2023-08-22 16:00:00', NULL, 102, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (103, 5, 3, 7, NULL, '2023-08-22', '2023-08-15 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (149, 'CONCLUIDO', 393.00, '2023-08-15 10:00:00', '2023-08-22 16:00:00', NULL, 103, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (104, 6, 1, 8, NULL, '2023-08-22', '2023-08-15 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (150, 'CONCLUIDO', 335.00, '2023-08-15 10:00:00', '2023-08-22 16:00:00', NULL, 104, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (105, NULL, 1, 6, NULL, '2023-09-22', '2023-09-16 09:00:00', 'RETIRADA', 'Carla Mendes', '11998765432', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (151, 'CONCLUIDO', 307.00, '2023-09-16 10:00:00', '2023-09-22 16:00:00', NULL, 105, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (106, NULL, 1, 7, NULL, '2023-09-22', '2023-09-16 09:00:00', 'RETIRADA', 'Fernanda Lima', '11976543210', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (152, 'CONCLUIDO', 319.00, '2023-09-16 10:00:00', '2023-09-22 16:00:00', NULL, 106, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (107, 6, 2, 8, NULL, '2023-09-22', '2023-09-16 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (153, 'CONCLUIDO', 366.00, '2023-09-16 10:00:00', '2023-09-22 16:00:00', NULL, 107, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (108, 5, 2, 7, NULL, '2023-10-22', '2023-10-12 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (154, 'CONCLUIDO', 350.00, '2023-10-12 10:00:00', '2023-10-22 16:00:00', NULL, 108, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (109, 6, 2, 8, NULL, '2023-10-22', '2023-10-12 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (155, 'CONCLUIDO', 362.00, '2023-10-12 10:00:00', '2023-10-22 16:00:00', NULL, 109, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (110, 1, 3, 1, NULL, '2023-10-22', '2023-10-12 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (156, 'CONCLUIDO', 409.00, '2023-10-12 10:00:00', '2023-10-22 16:00:00', NULL, 110, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (111, 4, 4, 4, NULL, '2023-10-22', '2023-10-12 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (157, 'CONCLUIDO', 456.00, '2023-10-12 10:00:00', '2023-10-22 16:00:00', NULL, 111, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (112, 6, 3, 8, NULL, '2023-11-22', '2023-11-13 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (158, 'CONCLUIDO', 393.00, '2023-11-13 10:00:00', '2023-11-22 16:00:00', NULL, 112, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (113, 1, 3, 1, NULL, '2023-11-22', '2023-11-13 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (159, 'CONCLUIDO', 405.00, '2023-11-13 10:00:00', '2023-11-22 16:00:00', NULL, 113, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (114, NULL, 4, 4, NULL, '2023-11-22', '2023-11-13 09:00:00', 'RETIRADA', 'Mariana Dias', '11911009988', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (160, 'CONCLUIDO', 452.00, '2023-11-13 10:00:00', '2023-11-22 16:00:00', NULL, 114, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (115, 1, 4, 1, NULL, '2023-12-22', '2023-12-14 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (161, 'CONCLUIDO', 436.00, '2023-12-14 10:00:00', '2023-12-22 16:00:00', NULL, 115, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (116, 4, 4, 4, NULL, '2023-12-22', '2023-12-14 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (162, 'CONCLUIDO', 448.00, '2023-12-14 10:00:00', '2023-12-22 16:00:00', NULL, 116, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (117, 1, 4, 4, NULL, '2023-12-22', '2023-12-14 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (163, 'CONCLUIDO', 460.00, '2023-12-14 10:00:00', '2023-12-22 16:00:00', NULL, 117, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (118, 3, 4, 5, NULL, '2023-12-22', '2023-12-14 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (164, 'CONCLUIDO', 472.00, '2023-12-14 10:00:00', '2023-12-22 16:00:00', NULL, 118, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (119, 4, 1, 6, NULL, '2023-12-22', '2023-12-14 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (165, 'CONCLUIDO', 379.00, '2023-12-14 10:00:00', '2023-12-22 16:00:00', NULL, 119, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (120, 5, 2, 7, NULL, '2023-12-22', '2023-12-14 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (166, 'CONCLUIDO', 426.00, '2023-12-14 10:00:00', '2023-12-22 16:00:00', NULL, 120, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (121, 4, 2, 6, NULL, '2024-01-22', '2024-01-13 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (167, 'CONCLUIDO', 278.00, '2024-01-13 10:00:00', '2024-01-22 16:00:00', NULL, 121, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (122, 5, 2, 7, NULL, '2024-01-22', '2024-01-13 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (168, 'CONCLUIDO', 290.00, '2024-01-13 10:00:00', '2024-01-22 16:00:00', NULL, 122, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (123, 6, 2, 8, NULL, '2024-01-22', '2024-01-13 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (169, 'CONCLUIDO', 302.00, '2024-01-13 10:00:00', '2024-01-22 16:00:00', NULL, 123, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (124, 1, 3, 1, NULL, '2024-01-22', '2024-01-13 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (170, 'CONCLUIDO', 349.00, '2024-01-13 10:00:00', '2024-01-22 16:00:00', NULL, 124, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (125, NULL, 3, 7, NULL, '2024-02-22', '2024-02-14 09:00:00', 'RETIRADA', 'Fernanda Lima', '11976543210', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (171, 'CONCLUIDO', 321.00, '2024-02-14 10:00:00', '2024-02-22 16:00:00', NULL, 125, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (126, NULL, 3, 8, NULL, '2024-02-22', '2024-02-14 09:00:00', 'RETIRADA', 'Ricardo Alves', '11965432109', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (172, 'CONCLUIDO', 333.00, '2024-02-14 10:00:00', '2024-02-22 16:00:00', NULL, 126, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (127, NULL, 3, 1, NULL, '2024-02-22', '2024-02-14 09:00:00', 'RETIRADA', 'Beatriz Santos', '11944332211', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (173, 'CONCLUIDO', 345.00, '2024-02-14 10:00:00', '2024-02-22 16:00:00', NULL, 127, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (128, 4, 4, 4, NULL, '2024-02-22', '2024-02-14 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (174, 'CONCLUIDO', 392.00, '2024-02-14 10:00:00', '2024-02-22 16:00:00', NULL, 128, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (129, 1, 1, 4, NULL, '2024-02-22', '2024-02-14 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (175, 'CONCLUIDO', 299.00, '2024-02-14 10:00:00', '2024-02-22 16:00:00', NULL, 129, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (130, 6, 4, 8, NULL, '2024-03-22', '2024-03-15 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (176, 'CONCLUIDO', 364.00, '2024-03-15 10:00:00', '2024-03-22 16:00:00', NULL, 130, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (131, 1, 4, 1, NULL, '2024-03-22', '2024-03-15 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (177, 'CONCLUIDO', 376.00, '2024-03-15 10:00:00', '2024-03-22 16:00:00', NULL, 131, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (132, 4, 4, 4, NULL, '2024-03-22', '2024-03-15 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (178, 'CONCLUIDO', 388.00, '2024-03-15 10:00:00', '2024-03-22 16:00:00', NULL, 132, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (133, 1, 1, 4, NULL, '2024-03-22', '2024-03-15 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (179, 'CONCLUIDO', 295.00, '2024-03-15 10:00:00', '2024-03-22 16:00:00', NULL, 133, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (134, 3, 1, 5, NULL, '2024-03-22', '2024-03-15 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (180, 'CONCLUIDO', 307.00, '2024-03-15 10:00:00', '2024-03-22 16:00:00', NULL, 134, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (135, NULL, 1, 1, NULL, '2024-04-22', '2024-04-16 09:00:00', 'RETIRADA', 'Beatriz Santos', '11944332211', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (181, 'CONCLUIDO', 267.00, '2024-04-16 10:00:00', '2024-04-22 16:00:00', NULL, 135, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (136, NULL, 1, 4, NULL, '2024-04-22', '2024-04-16 09:00:00', 'RETIRADA', 'Mariana Dias', '11911009988', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (182, 'CONCLUIDO', 279.00, '2024-04-16 10:00:00', '2024-04-22 16:00:00', NULL, 136, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (137, NULL, 1, 4, NULL, '2024-04-22', '2024-04-16 09:00:00', 'RETIRADA', 'João Souza', '11987654321', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (183, 'CONCLUIDO', 291.00, '2024-04-16 10:00:00', '2024-04-22 16:00:00', NULL, 137, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (138, 3, 3, 5, NULL, '2024-04-22', '2024-04-16 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (184, 'CONCLUIDO', 373.00, '2024-04-16 10:00:00', '2024-04-22 16:00:00', NULL, 138, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (139, 4, 4, 6, NULL, '2024-04-22', '2024-04-16 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (185, 'CONCLUIDO', 420.00, '2024-04-16 10:00:00', '2024-04-22 16:00:00', NULL, 139, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (140, 4, 2, 4, NULL, '2024-05-22', '2024-05-12 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (186, 'CONCLUIDO', 310.00, '2024-05-12 10:00:00', '2024-05-22 16:00:00', NULL, 140, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (141, 1, 2, 4, NULL, '2024-05-22', '2024-05-12 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (187, 'CONCLUIDO', 322.00, '2024-05-12 10:00:00', '2024-05-22 16:00:00', NULL, 141, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (142, 3, 2, 5, NULL, '2024-05-22', '2024-05-12 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (188, 'CONCLUIDO', 334.00, '2024-05-12 10:00:00', '2024-05-22 16:00:00', NULL, 142, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (143, 4, 4, 6, NULL, '2024-05-22', '2024-05-12 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (189, 'CONCLUIDO', 416.00, '2024-05-12 10:00:00', '2024-05-22 16:00:00', NULL, 143, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (144, 1, 3, 4, NULL, '2024-06-22', '2024-06-13 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (190, 'CONCLUIDO', 353.00, '2024-06-13 10:00:00', '2024-06-22 16:00:00', NULL, 144, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (145, 3, 3, 5, NULL, '2024-06-22', '2024-06-13 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (191, 'CONCLUIDO', 365.00, '2024-06-13 10:00:00', '2024-06-22 16:00:00', NULL, 145, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (146, 4, 3, 6, NULL, '2024-06-22', '2024-06-13 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (192, 'CONCLUIDO', 377.00, '2024-06-13 10:00:00', '2024-06-22 16:00:00', NULL, 146, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (147, 5, 3, 7, NULL, '2024-06-22', '2024-06-13 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (193, 'CONCLUIDO', 389.00, '2024-06-13 10:00:00', '2024-06-22 16:00:00', NULL, 147, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (148, 6, 3, 8, NULL, '2024-06-22', '2024-06-13 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (194, 'CONCLUIDO', 401.00, '2024-06-13 10:00:00', '2024-06-22 16:00:00', NULL, 148, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (149, 1, 1, 1, NULL, '2024-06-22', '2024-06-13 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (195, 'CONCLUIDO', 343.00, '2024-06-13 10:00:00', '2024-06-22 16:00:00', NULL, 149, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (150, 4, 2, 4, NULL, '2024-06-22', '2024-06-13 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (196, 'CONCLUIDO', 390.00, '2024-06-13 10:00:00', '2024-06-22 16:00:00', NULL, 150, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (151, 3, 4, 5, NULL, '2024-07-22', '2024-07-14 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (197, 'CONCLUIDO', 396.00, '2024-07-14 10:00:00', '2024-07-22 16:00:00', NULL, 151, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (152, 4, 4, 6, NULL, '2024-07-22', '2024-07-14 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (198, 'CONCLUIDO', 408.00, '2024-07-14 10:00:00', '2024-07-22 16:00:00', NULL, 152, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (153, 5, 4, 7, NULL, '2024-07-22', '2024-07-14 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (199, 'CONCLUIDO', 420.00, '2024-07-14 10:00:00', '2024-07-22 16:00:00', NULL, 153, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (154, 6, 2, 8, NULL, '2024-07-22', '2024-07-14 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (200, 'CONCLUIDO', 362.00, '2024-07-14 10:00:00', '2024-07-22 16:00:00', NULL, 154, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (155, 4, 1, 6, NULL, '2024-08-22', '2024-08-15 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (201, 'CONCLUIDO', 299.00, '2024-08-15 10:00:00', '2024-08-22 16:00:00', NULL, 155, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (156, 5, 1, 7, NULL, '2024-08-22', '2024-08-15 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (202, 'CONCLUIDO', 311.00, '2024-08-15 10:00:00', '2024-08-22 16:00:00', NULL, 156, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (157, 6, 1, 8, NULL, '2024-08-22', '2024-08-15 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (203, 'CONCLUIDO', 323.00, '2024-08-15 10:00:00', '2024-08-22 16:00:00', NULL, 157, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (158, 1, 4, 1, NULL, '2024-08-22', '2024-08-15 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (204, 'CONCLUIDO', 440.00, '2024-08-15 10:00:00', '2024-08-22 16:00:00', NULL, 158, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (159, NULL, 2, 4, NULL, '2024-08-22', '2024-08-15 09:00:00', 'RETIRADA', 'Mariana Dias', '11911009988', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (205, 'CONCLUIDO', 382.00, '2024-08-15 10:00:00', '2024-08-22 16:00:00', NULL, 159, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (160, 5, 2, 7, NULL, '2024-09-22', '2024-09-16 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (206, 'CONCLUIDO', 342.00, '2024-09-16 10:00:00', '2024-09-22 16:00:00', NULL, 160, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (161, 6, 2, 8, NULL, '2024-09-22', '2024-09-16 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (207, 'CONCLUIDO', 354.00, '2024-09-16 10:00:00', '2024-09-22 16:00:00', NULL, 161, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (162, 1, 2, 1, NULL, '2024-09-22', '2024-09-16 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (208, 'CONCLUIDO', 366.00, '2024-09-16 10:00:00', '2024-09-22 16:00:00', NULL, 162, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (163, NULL, 1, 4, NULL, '2024-09-22', '2024-09-16 09:00:00', 'RETIRADA', 'Mariana Dias', '11911009988', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (209, 'CONCLUIDO', 343.00, '2024-09-16 10:00:00', '2024-09-22 16:00:00', NULL, 163, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (164, NULL, 1, 4, NULL, '2024-09-22', '2024-09-16 09:00:00', 'RETIRADA', 'João Souza', '11987654321', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (210, 'CONCLUIDO', 355.00, '2024-09-16 10:00:00', '2024-09-22 16:00:00', NULL, 164, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (165, 6, 3, 8, NULL, '2024-10-22', '2024-10-12 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (211, 'CONCLUIDO', 385.00, '2024-10-12 10:00:00', '2024-10-22 16:00:00', NULL, 165, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (166, 1, 3, 1, NULL, '2024-10-22', '2024-10-12 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (212, 'CONCLUIDO', 397.00, '2024-10-12 10:00:00', '2024-10-22 16:00:00', NULL, 166, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (167, 4, 3, 4, NULL, '2024-10-22', '2024-10-12 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (213, 'CONCLUIDO', 409.00, '2024-10-12 10:00:00', '2024-10-22 16:00:00', NULL, 167, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (168, 1, 2, 4, NULL, '2024-10-22', '2024-10-12 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (214, 'CONCLUIDO', 386.00, '2024-10-12 10:00:00', '2024-10-22 16:00:00', NULL, 168, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (169, 3, 4, 5, NULL, '2024-10-22', '2024-10-12 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (215, 'CONCLUIDO', 468.00, '2024-10-12 10:00:00', '2024-10-22 16:00:00', NULL, 169, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (170, NULL, 4, 1, NULL, '2024-11-22', '2024-11-13 09:00:00', 'RETIRADA', 'Beatriz Santos', '11944332211', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (216, 'CONCLUIDO', 428.00, '2024-11-13 10:00:00', '2024-11-22 16:00:00', NULL, 170, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (171, NULL, 4, 4, NULL, '2024-11-22', '2024-11-13 09:00:00', 'RETIRADA', 'Mariana Dias', '11911009988', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (217, 'CONCLUIDO', 440.00, '2024-11-13 10:00:00', '2024-11-22 16:00:00', NULL, 171, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (172, NULL, 4, 4, NULL, '2024-11-22', '2024-11-13 09:00:00', 'RETIRADA', 'João Souza', '11987654321', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (218, 'CONCLUIDO', 452.00, '2024-11-13 10:00:00', '2024-11-22 16:00:00', NULL, 172, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (173, 3, 3, 5, NULL, '2024-11-22', '2024-11-13 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (219, 'CONCLUIDO', 429.00, '2024-11-13 10:00:00', '2024-11-22 16:00:00', NULL, 173, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (174, 4, 1, 4, NULL, '2024-12-22', '2024-12-14 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (220, 'CONCLUIDO', 331.00, '2024-12-14 10:00:00', '2024-12-22 16:00:00', NULL, 174, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (175, 1, 1, 4, NULL, '2024-12-22', '2024-12-14 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (221, 'CONCLUIDO', 343.00, '2024-12-14 10:00:00', '2024-12-22 16:00:00', NULL, 175, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (176, 3, 1, 5, NULL, '2024-12-22', '2024-12-14 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (222, 'CONCLUIDO', 355.00, '2024-12-14 10:00:00', '2024-12-22 16:00:00', NULL, 176, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (177, 4, 1, 6, NULL, '2024-12-22', '2024-12-14 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (223, 'CONCLUIDO', 367.00, '2024-12-14 10:00:00', '2024-12-22 16:00:00', NULL, 177, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (178, 5, 1, 7, NULL, '2024-12-22', '2024-12-14 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (224, 'CONCLUIDO', 379.00, '2024-12-14 10:00:00', '2024-12-22 16:00:00', NULL, 178, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (179, 6, 2, 8, NULL, '2024-12-22', '2024-12-14 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (225, 'CONCLUIDO', 426.00, '2024-12-14 10:00:00', '2024-12-22 16:00:00', NULL, 179, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (180, NULL, 3, 1, NULL, '2024-12-22', '2024-12-14 09:00:00', 'RETIRADA', 'Beatriz Santos', '11944332211', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (226, 'CONCLUIDO', 473.00, '2024-12-14 10:00:00', '2024-12-22 16:00:00', NULL, 180, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (181, 5, 3, 7, NULL, '2025-01-22', '2025-01-13 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (227, 'CONCLUIDO', 313.00, '2025-01-13 10:00:00', '2025-01-22 16:00:00', NULL, 181, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (182, 6, 3, 8, NULL, '2025-01-22', '2025-01-13 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (228, 'CONCLUIDO', 325.00, '2025-01-13 10:00:00', '2025-01-22 16:00:00', NULL, 182, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (183, 1, 3, 1, NULL, '2025-01-22', '2025-01-13 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (229, 'CONCLUIDO', 337.00, '2025-01-13 10:00:00', '2025-01-22 16:00:00', NULL, 183, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (184, 4, 3, 4, NULL, '2025-01-22', '2025-01-13 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (230, 'CONCLUIDO', 349.00, '2025-01-13 10:00:00', '2025-01-22 16:00:00', NULL, 184, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (185, 1, 3, 4, NULL, '2025-01-22', '2025-01-13 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (231, 'CONCLUIDO', 361.00, '2025-01-13 10:00:00', '2025-01-22 16:00:00', NULL, 185, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (186, 3, 2, 5, NULL, '2025-01-22', '2025-01-13 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (232, 'CONCLUIDO', 338.00, '2025-01-13 10:00:00', '2025-01-22 16:00:00', NULL, 186, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (187, 6, 4, 8, NULL, '2025-02-22', '2025-02-14 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (233, 'CONCLUIDO', 356.00, '2025-02-14 10:00:00', '2025-02-22 16:00:00', NULL, 187, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (188, 1, 4, 1, NULL, '2025-02-22', '2025-02-14 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (234, 'CONCLUIDO', 368.00, '2025-02-14 10:00:00', '2025-02-22 16:00:00', NULL, 188, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (189, 4, 4, 4, NULL, '2025-02-22', '2025-02-14 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (235, 'CONCLUIDO', 380.00, '2025-02-14 10:00:00', '2025-02-22 16:00:00', NULL, 189, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (190, 1, 4, 4, NULL, '2025-02-22', '2025-02-14 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (236, 'CONCLUIDO', 392.00, '2025-02-14 10:00:00', '2025-02-22 16:00:00', NULL, 190, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (191, 3, 4, 5, NULL, '2025-02-22', '2025-02-14 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (237, 'CONCLUIDO', 404.00, '2025-02-14 10:00:00', '2025-02-22 16:00:00', NULL, 191, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (192, NULL, 3, 6, NULL, '2025-02-22', '2025-02-14 09:00:00', 'RETIRADA', 'Carla Mendes', '11998765432', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (238, 'CONCLUIDO', 381.00, '2025-02-14 10:00:00', '2025-02-22 16:00:00', NULL, 192, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (193, 5, 1, 7, NULL, '2025-02-22', '2025-02-14 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (239, 'CONCLUIDO', 323.00, '2025-02-14 10:00:00', '2025-02-22 16:00:00', NULL, 193, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (194, 1, 1, 1, NULL, '2025-03-22', '2025-03-15 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (240, 'CONCLUIDO', 259.00, '2025-03-15 10:00:00', '2025-03-22 16:00:00', NULL, 194, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (195, 4, 1, 4, NULL, '2025-03-22', '2025-03-15 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (241, 'CONCLUIDO', 271.00, '2025-03-15 10:00:00', '2025-03-22 16:00:00', NULL, 195, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (196, 1, 1, 4, NULL, '2025-03-22', '2025-03-15 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (242, 'CONCLUIDO', 283.00, '2025-03-15 10:00:00', '2025-03-22 16:00:00', NULL, 196, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (197, 3, 1, 5, NULL, '2025-03-22', '2025-03-15 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (243, 'CONCLUIDO', 295.00, '2025-03-15 10:00:00', '2025-03-22 16:00:00', NULL, 197, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (198, 4, 1, 6, NULL, '2025-03-22', '2025-03-15 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (244, 'CONCLUIDO', 307.00, '2025-03-15 10:00:00', '2025-03-22 16:00:00', NULL, 198, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (199, NULL, 2, 7, NULL, '2025-03-22', '2025-03-15 09:00:00', 'RETIRADA', 'Fernanda Lima', '11976543210', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (245, 'CONCLUIDO', 354.00, '2025-03-15 10:00:00', '2025-03-22 16:00:00', NULL, 199, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (200, NULL, 2, 8, NULL, '2025-03-22', '2025-03-15 09:00:00', 'RETIRADA', 'Ricardo Alves', '11965432109', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (246, 'CONCLUIDO', 366.00, '2025-03-15 10:00:00', '2025-03-22 16:00:00', NULL, 200, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (201, 4, 2, 4, NULL, '2025-04-22', '2025-04-16 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (247, 'CONCLUIDO', 302.00, '2025-04-16 10:00:00', '2025-04-22 16:00:00', NULL, 201, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (202, 1, 2, 4, NULL, '2025-04-22', '2025-04-16 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (248, 'CONCLUIDO', 314.00, '2025-04-16 10:00:00', '2025-04-22 16:00:00', NULL, 202, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (203, 3, 2, 5, NULL, '2025-04-22', '2025-04-16 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (249, 'CONCLUIDO', 326.00, '2025-04-16 10:00:00', '2025-04-22 16:00:00', NULL, 203, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (204, 4, 2, 6, NULL, '2025-04-22', '2025-04-16 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (250, 'CONCLUIDO', 338.00, '2025-04-16 10:00:00', '2025-04-22 16:00:00', NULL, 204, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (205, 5, 2, 7, NULL, '2025-04-22', '2025-04-16 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (251, 'CONCLUIDO', 350.00, '2025-04-16 10:00:00', '2025-04-22 16:00:00', NULL, 205, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (206, 6, 3, 8, NULL, '2025-04-22', '2025-04-16 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (252, 'CONCLUIDO', 397.00, '2025-04-16 10:00:00', '2025-04-22 16:00:00', NULL, 206, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (207, 1, 4, 1, NULL, '2025-04-22', '2025-04-16 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (253, 'CONCLUIDO', 444.00, '2025-04-16 10:00:00', '2025-04-22 16:00:00', NULL, 207, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (208, 1, 3, 4, NULL, '2025-05-22', '2025-05-12 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (254, 'CONCLUIDO', 345.00, '2025-05-12 10:00:00', '2025-05-22 16:00:00', NULL, 208, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (209, 3, 3, 5, NULL, '2025-05-22', '2025-05-12 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (255, 'CONCLUIDO', 357.00, '2025-05-12 10:00:00', '2025-05-22 16:00:00', NULL, 209, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (210, 4, 3, 6, NULL, '2025-05-22', '2025-05-12 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (256, 'CONCLUIDO', 369.00, '2025-05-12 10:00:00', '2025-05-22 16:00:00', NULL, 210, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (211, 5, 3, 7, NULL, '2025-05-22', '2025-05-12 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (257, 'CONCLUIDO', 381.00, '2025-05-12 10:00:00', '2025-05-22 16:00:00', NULL, 211, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (212, 6, 3, 8, NULL, '2025-05-22', '2025-05-12 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (258, 'CONCLUIDO', 393.00, '2025-05-12 10:00:00', '2025-05-22 16:00:00', NULL, 212, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (213, 1, 4, 1, NULL, '2025-05-22', '2025-05-12 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (259, 'CONCLUIDO', 440.00, '2025-05-12 10:00:00', '2025-05-22 16:00:00', NULL, 213, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (214, NULL, 4, 5, NULL, '2025-06-22', '2025-06-13 09:00:00', 'RETIRADA', 'Ana Oliveira', '11912345678', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (260, 'CONCLUIDO', 388.00, '2025-06-13 10:00:00', '2025-06-22 16:00:00', NULL, 214, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (215, NULL, 4, 6, NULL, '2025-06-22', '2025-06-13 09:00:00', 'RETIRADA', 'Carla Mendes', '11998765432', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (261, 'CONCLUIDO', 400.00, '2025-06-13 10:00:00', '2025-06-22 16:00:00', NULL, 215, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (216, NULL, 4, 7, NULL, '2025-06-22', '2025-06-13 09:00:00', 'RETIRADA', 'Fernanda Lima', '11976543210', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (262, 'CONCLUIDO', 412.00, '2025-06-13 10:00:00', '2025-06-22 16:00:00', NULL, 216, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (217, NULL, 4, 8, NULL, '2025-06-22', '2025-06-13 09:00:00', 'RETIRADA', 'Ricardo Alves', '11965432109', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (263, 'CONCLUIDO', 424.00, '2025-06-13 10:00:00', '2025-06-22 16:00:00', NULL, 217, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (218, NULL, 4, 1, NULL, '2025-06-22', '2025-06-13 09:00:00', 'RETIRADA', 'Beatriz Santos', '11944332211', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (264, 'CONCLUIDO', 436.00, '2025-06-13 10:00:00', '2025-06-22 16:00:00', NULL, 218, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (219, NULL, 4, 4, NULL, '2025-06-22', '2025-06-13 09:00:00', 'RETIRADA', 'Mariana Dias', '11911009988', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (265, 'CONCLUIDO', 448.00, '2025-06-13 10:00:00', '2025-06-22 16:00:00', NULL, 219, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (220, NULL, 4, 4, NULL, '2025-06-22', '2025-06-13 09:00:00', 'RETIRADA', 'João Souza', '11987654321', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (266, 'CONCLUIDO', 460.00, '2025-06-13 10:00:00', '2025-06-22 16:00:00', NULL, 220, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (221, 3, 1, 5, NULL, '2025-06-22', '2025-06-13 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (267, 'CONCLUIDO', 367.00, '2025-06-13 10:00:00', '2025-06-22 16:00:00', NULL, 221, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (222, 4, 2, 6, NULL, '2025-06-22', '2025-06-13 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (268, 'CONCLUIDO', 414.00, '2025-06-13 10:00:00', '2025-06-22 16:00:00', NULL, 222, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (223, 4, 1, 6, NULL, '2025-07-22', '2025-07-14 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (269, 'CONCLUIDO', 291.00, '2025-07-14 10:00:00', '2025-07-22 16:00:00', NULL, 223, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (224, 5, 1, 7, NULL, '2025-07-22', '2025-07-14 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (270, 'CONCLUIDO', 303.00, '2025-07-14 10:00:00', '2025-07-22 16:00:00', NULL, 224, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (225, 6, 1, 8, NULL, '2025-07-22', '2025-07-14 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (271, 'CONCLUIDO', 315.00, '2025-07-14 10:00:00', '2025-07-22 16:00:00', NULL, 225, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (226, 1, 1, 1, NULL, '2025-07-22', '2025-07-14 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (272, 'CONCLUIDO', 327.00, '2025-07-14 10:00:00', '2025-07-22 16:00:00', NULL, 226, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (227, 4, 1, 4, NULL, '2025-07-22', '2025-07-14 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (273, 'CONCLUIDO', 339.00, '2025-07-14 10:00:00', '2025-07-22 16:00:00', NULL, 227, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (228, NULL, 3, 4, NULL, '2025-07-22', '2025-07-14 09:00:00', 'RETIRADA', 'João Souza', '11987654321', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (274, 'CONCLUIDO', 421.00, '2025-07-14 10:00:00', '2025-07-22 16:00:00', NULL, 228, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (229, NULL, 2, 7, NULL, '2025-08-22', '2025-08-15 09:00:00', 'RETIRADA', 'Fernanda Lima', '11976543210', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (275, 'CONCLUIDO', 334.00, '2025-08-15 10:00:00', '2025-08-22 16:00:00', NULL, 229, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (230, NULL, 2, 8, NULL, '2025-08-22', '2025-08-15 09:00:00', 'RETIRADA', 'Ricardo Alves', '11965432109', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (276, 'CONCLUIDO', 346.00, '2025-08-15 10:00:00', '2025-08-22 16:00:00', NULL, 230, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (231, NULL, 2, 1, NULL, '2025-08-22', '2025-08-15 09:00:00', 'RETIRADA', 'Beatriz Santos', '11944332211', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (277, 'CONCLUIDO', 358.00, '2025-08-15 10:00:00', '2025-08-22 16:00:00', NULL, 231, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (232, NULL, 2, 4, NULL, '2025-08-22', '2025-08-15 09:00:00', 'RETIRADA', 'Mariana Dias', '11911009988', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (278, 'CONCLUIDO', 370.00, '2025-08-15 10:00:00', '2025-08-22 16:00:00', NULL, 232, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (233, NULL, 2, 4, NULL, '2025-08-22', '2025-08-15 09:00:00', 'RETIRADA', 'João Souza', '11987654321', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (279, 'CONCLUIDO', 382.00, '2025-08-15 10:00:00', '2025-08-22 16:00:00', NULL, 233, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (234, 3, 4, 5, NULL, '2025-08-22', '2025-08-15 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (280, 'CONCLUIDO', 464.00, '2025-08-15 10:00:00', '2025-08-22 16:00:00', NULL, 234, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (235, 4, 1, 6, NULL, '2025-08-22', '2025-08-15 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (281, 'CONCLUIDO', 371.00, '2025-08-15 10:00:00', '2025-08-22 16:00:00', NULL, 235, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (236, 6, 3, 8, NULL, '2025-09-22', '2025-09-16 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (282, 'PAGO', 377.00, '2025-09-16 10:00:00', '2025-09-22 16:00:00', NULL, 236, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (237, 1, 3, 1, NULL, '2025-09-22', '2025-09-16 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (283, 'PAGO', 389.00, '2025-09-16 10:00:00', '2025-09-22 16:00:00', NULL, 237, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (238, 4, 3, 4, NULL, '2025-09-22', '2025-09-16 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (284, 'PAGO', 401.00, '2025-09-16 10:00:00', '2025-09-22 16:00:00', NULL, 238, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (239, 1, 3, 4, NULL, '2025-09-22', '2025-09-16 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (285, 'PAGO', 413.00, '2025-09-16 10:00:00', '2025-09-22 16:00:00', NULL, 239, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (240, 3, 3, 5, NULL, '2025-09-22', '2025-09-16 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (286, 'PAGO', 425.00, '2025-09-16 10:00:00', '2025-09-22 16:00:00', NULL, 240, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (241, NULL, 1, 6, NULL, '2025-09-22', '2025-09-16 09:00:00', 'RETIRADA', 'Carla Mendes', '11998765432', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (287, 'PAGO', 367.00, '2025-09-16 10:00:00', '2025-09-22 16:00:00', NULL, 241, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (242, NULL, 1, 7, NULL, '2025-09-22', '2025-09-16 09:00:00', 'RETIRADA', 'Fernanda Lima', '11976543210', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (288, 'PAGO', 379.00, '2025-09-16 10:00:00', '2025-09-22 16:00:00', NULL, 242, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (243, 1, 4, 1, NULL, '2025-10-22', '2025-10-12 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (289, 'PAGO', 420.00, '2025-10-12 10:00:00', '2025-10-22 16:00:00', NULL, 243, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (244, 4, 4, 4, NULL, '2025-10-22', '2025-10-12 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (290, 'PAGO', 432.00, '2025-10-12 10:00:00', '2025-10-22 16:00:00', NULL, 244, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (245, 1, 4, 4, NULL, '2025-10-22', '2025-10-12 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (291, 'PAGO', 444.00, '2025-10-12 10:00:00', '2025-10-22 16:00:00', NULL, 245, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (246, 3, 4, 5, NULL, '2025-10-22', '2025-10-12 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (292, 'PAGO', 456.00, '2025-10-12 10:00:00', '2025-10-22 16:00:00', NULL, 246, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (247, 4, 4, 6, NULL, '2025-10-22', '2025-10-12 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (293, 'PAGO', 468.00, '2025-10-12 10:00:00', '2025-10-22 16:00:00', NULL, 247, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (248, 5, 2, 7, NULL, '2025-10-22', '2025-10-12 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (294, 'PAGO', 410.00, '2025-10-12 10:00:00', '2025-10-22 16:00:00', NULL, 248, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (249, 6, 3, 8, NULL, '2025-10-22', '2025-10-12 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (295, 'PAGO', 457.00, '2025-10-12 10:00:00', '2025-10-22 16:00:00', NULL, 249, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (250, 4, 1, 4, NULL, '2025-11-22', '2025-11-13 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (296, 'PAGO', 323.00, '2025-11-13 10:00:00', '2025-11-22 16:00:00', NULL, 250, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (251, 1, 1, 4, NULL, '2025-11-22', '2025-11-13 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (297, 'PAGO', 335.00, '2025-11-13 10:00:00', '2025-11-22 16:00:00', NULL, 251, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (252, 3, 1, 5, NULL, '2025-11-22', '2025-11-13 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (298, 'PAGO', 347.00, '2025-11-13 10:00:00', '2025-11-22 16:00:00', NULL, 252, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (253, 4, 1, 6, NULL, '2025-11-22', '2025-11-13 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (299, 'PAGO', 359.00, '2025-11-13 10:00:00', '2025-11-22 16:00:00', NULL, 253, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (254, 5, 1, 7, NULL, '2025-11-22', '2025-11-13 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (300, 'PAGO', 371.00, '2025-11-13 10:00:00', '2025-11-22 16:00:00', NULL, 254, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (255, NULL, 4, 8, NULL, '2025-11-22', '2025-11-13 09:00:00', 'RETIRADA', 'Ricardo Alves', '11965432109', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (301, 'PAGO', 488.00, '2025-11-13 10:00:00', '2025-11-22 16:00:00', NULL, 255, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (256, 1, 2, 4, NULL, '2025-12-22', '2025-12-14 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (302, 'PAGO', 366.00, '2025-12-14 10:00:00', '2025-12-22 16:00:00', NULL, 256, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (257, 3, 2, 5, NULL, '2025-12-22', '2025-12-14 09:00:00', 'ENTREGA', 'Ana Oliveira', '11912345678', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (303, 'PAGO', 378.00, '2025-12-14 10:00:00', '2025-12-22 16:00:00', NULL, 257, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (258, 4, 2, 6, NULL, '2025-12-22', '2025-12-14 09:00:00', 'ENTREGA', 'Carla Mendes', '11998765432', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (304, 'PAGO', 390.00, '2025-12-14 10:00:00', '2025-12-22 16:00:00', NULL, 258, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (259, 5, 2, 7, NULL, '2025-12-22', '2025-12-14 09:00:00', 'ENTREGA', 'Fernanda Lima', '11976543210', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (305, 'PAGO', 402.00, '2025-12-14 10:00:00', '2025-12-22 16:00:00', NULL, 259, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (260, 6, 2, 8, NULL, '2025-12-22', '2025-12-14 09:00:00', 'ENTREGA', 'Ricardo Alves', '11965432109', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (306, 'PAGO', 414.00, '2025-12-14 10:00:00', '2025-12-22 16:00:00', NULL, 260, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (261, 1, 2, 1, NULL, '2025-12-22', '2025-12-14 09:00:00', 'ENTREGA', 'Beatriz Santos', '11944332211', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (307, 'PAGO', 426.00, '2025-12-14 10:00:00', '2025-12-22 16:00:00', NULL, 261, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (262, 4, 2, 4, NULL, '2025-12-22', '2025-12-14 09:00:00', 'ENTREGA', 'Mariana Dias', '11911009988', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (308, 'PAGO', 438.00, '2025-12-14 10:00:00', '2025-12-22 16:00:00', NULL, 262, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (263, 1, 1, 4, NULL, '2025-12-22', '2025-12-14 09:00:00', 'ENTREGA', 'João Souza', '11987654321', NULL, 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (309, 'PAGO', 415.00, '2025-12-14 10:00:00', '2025-12-22 16:00:00', NULL, 263, 1);
INSERT INTO teiko.pedido_bolo (id, endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, horario_retirada, is_ativo) VALUES (264, NULL, 3, 5, NULL, '2025-12-22', '2025-12-14 09:00:00', 'RETIRADA', 'Ana Oliveira', '11912345678', '17:00', 1);
INSERT INTO teiko.resumo_pedido (id, status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES (310, 'PAGO', 497.00, '2025-12-14 10:00:00', '2025-12-22 16:00:00', NULL, 264, 1);

-- Compatibilidade com PedidoBoloEntity (horario_retirada em RETIRADA); ignora se ja existir (continue-on-error)
ALTER TABLE teiko.pedido_bolo ADD COLUMN horario_retirada VARCHAR(10) NULL AFTER telefone_cliente;
UPDATE teiko.pedido_bolo SET horario_retirada = '10:00' WHERE tipo_entrega = 'RETIRADA' AND horario_retirada IS NULL;

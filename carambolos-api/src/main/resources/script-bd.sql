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
-- DADOS INICIAIS
-- =====================================================

-- Usuários
INSERT INTO teiko.usuario (nome, senha, contato, sys_admin, is_ativo) VALUES
('Admin Sistema', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '11999999999', 1, 1),
('Murilo', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '5511912345671', 1, 1),
('João Souza', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '11988888888', 0, 1),
('Ana Oliveira', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '11977777777', 0, 1);

-- Endereços
INSERT INTO teiko.endereco (cep, estado, cidade, bairro, logradouro, numero, complemento, referencia, usuario_id, is_ativo) VALUES
('01234567', 'SP', 'São Paulo', 'Centro', 'Rua A', '100', NULL, 'Perto da praça', 1, 1),
('76543210', 'SP', 'São Paulo', 'Bela Vista', 'Rua B', '200', NULL, 'Próximo ao mercado', 2, 1),
('12345678', 'SP', 'São Paulo', 'Pinheiros', 'Rua C', '300', NULL, NULL, 3, 1);

-- Produtos Fornada
INSERT INTO teiko.produto_fornada (produto, descricao, valor, categoria, is_ativo) VALUES
('Pão de Queijo', 'Pão de queijo artesanal', 8.00, 'Salgados', 1),
('Pão Francês', 'Pão francês crocante', 5.00, 'Padaria', 1),
('Croissant', 'Croissant de manteiga', 12.00, 'Salgados', 1),
('Brioche', 'Brioche doce', 10.00, 'Doces', 1);

-- Imagens dos Produtos Fornada
INSERT INTO teiko.imagem_produto_fornada (produto_fornada_id, url) VALUES
-- Pão de Queijo
(1, 'https://picsum.photos/seed/paoqueijo1/320/320'),
(1, 'https://picsum.photos/seed/paoqueijo2/320/320'),
-- Pão Francês
(2, 'https://picsum.photos/seed/paofrances1/320/320'),
(2, 'https://picsum.photos/seed/paofrances2/320/320'),
-- Croissant
(3, 'https://picsum.photos/seed/croissant1/320/320'),
(3, 'https://picsum.photos/seed/croissant2/320/320'),
-- Brioche
(4, 'https://picsum.photos/seed/brioche1/320/320'),
(4, 'https://picsum.photos/seed/brioche2/320/320');

-- Fornadas
INSERT INTO teiko.fornada (data_inicio, data_fim, is_ativo) VALUES
('2024-12-01', '2024-12-02', 1),
('2024-12-03', '2024-12-04', 1),
('2024-12-05', '2024-12-06', 1);

-- Fornada da Vez
INSERT INTO teiko.fornada_da_vez (produto_fornada_id, fornada_id, quantidade, is_ativo) VALUES
(1, 1, 100, 1),
(2, 1, 100, 1),
(2, 2, 80, 1),
(3, 3, 50, 1),
(4, 3, 30, 1);

-- Regras de venda simuladas para fornadas:
-- Fornadas 1 e 3: todos os itens vendidos
-- Fornada 2: itens vencidos (baixa venda) => representado por poucos pedidos

-- Pedidos Fornada adicionais para vender toda a quantidade das fornadas 1 e 3
INSERT INTO teiko.pedido_fornada (fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES
(1, 1, 1, 90, '2024-12-12', 'ENTREGA', 'Cliente 1', '11911111111', 1), -- soma 90 + 10 (já existente) = 100
(3, 2, 2, 50, '2024-12-06', 'RETIRADA', 'Cliente 2', '11922222222', 1),
(4, 3, 3, 30, '2024-12-06', 'RETIRADA', 'Cliente 3', '11933333333', 1);

-- Pedidos adicionais para garantir IDs suficientes (FKs nos resumos 2025 usam até id 8)
INSERT INTO teiko.pedido_fornada (fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES
(2, 1, 1, 5,  '2025-01-05', 'ENTREGA', 'Cliente 4', '11944444444', 1),
(2, 2, 2, 10, '2025-10-10', 'RETIRADA', 'Cliente 5', '11955555555', 1);

-- Massas
INSERT INTO teiko.massa (sabor, valor, is_ativo) VALUES
('cacau', 5.00, 1),
('cacau_expresso', 5.00, 1),
('baunilha', 5.00, 1),
('red_velvet', 5.00, 1);

-- Coberturas
INSERT INTO teiko.cobertura (cor, descricao, is_ativo) VALUES
('Branco', 'Cobertura cremosa de baunilha', 1),
('Preto', 'Cobertura de chocolate meio amargo', 1),
('Rosa', 'Cobertura de morango com brilho', 1);

-- Decorações
INSERT INTO teiko.decoracao (observacao, nome, categoria, is_ativo) VALUES
('Decoração com tema de festa junina', 'Flores Silvestres', 'Vintage', 1),
('Decoração com flores naturais', 'Festa Junina', 'Floral', 1),
('Decoração com tema de aniversário infantil', 'Aniversário Infantil', 'My Carambolo', 1),
('Decoração elegante para casamento', 'Casamento Elegante', 'Shag Cake', 1);

-- Imagens Decoração
INSERT INTO teiko.imagem_decoracao (decoracao_id, url) VALUES
-- Natureza (flores/folhagens)
(1, 'https://picsum.photos/seed/nature1/320/320'),
(1, 'https://picsum.photos/seed/nature2/320/320'),
-- Festa Junina (temática)
(2, 'https://picsum.photos/seed/festajunina1/320/320'),
(2, 'https://picsum.photos/seed/festajunina2/320/320'),
-- Infantil (ilustrações suaves)
(3, 'https://picsum.photos/seed/infantil1/320/320'),
(3, 'https://picsum.photos/seed/infantil2/320/320'),
-- Casamento (mantém uma imagem que já estava carregando)
(4, 'https://images.unsplash.com/photo-1519225421980-715cb0215aed?w=320&h=320&fit=crop&crop=center'),
(4, 'https://picsum.photos/seed/casamento1/320/320');
-- Recheios Unitários
INSERT INTO teiko.recheio_unitario (sabor, descricao, valor, is_ativo) VALUES
('creamcheese_frosting', 'Creamcheese Frosting', 10.00, 1),
('devil_s_cake_ganache_meio_amargo', 'Devil''s Cake (Ganache meio-amargo)', 10.00, 1),
('zanza_ganache_meio_amargo_e_reducao_de_frutas_vermelhas', 'Zanza (Ganache meio-amargo e redução de frutas vermelhas)', 10.00, 1),
('brunna_brigadeiro_de_limao_siciliano', 'Brunna (Brigadeiro de limão siciliano)', 10.00, 1),
('marilia_brigadeiro_meio_amargo', 'Marilia (Brigadeiro meio-amargo)', 10.00, 1),
('hugo_brigadeiro_meio_amargo_e_brigadeiro_de_ninho', 'Hugo (Brigadeiro meio-amargo e Brigadeiro de ninho)', 10.00, 1),
('bia_benego_cocada_cremosa_de_coco_queimado', 'Bia Benego (Cocada cremosa de coco queimado)', 10.00, 1),
('duda_brigadeiro_de_doce_de_leite', 'Duda (Brigadeiro de Doce de leite)', 10.00, 1),
('giovanna_brigadeiro_de_pistache', 'Giovanna (Brigadeiro de Pistache)', 10.00, 1),
('juliana_creme_4_leites_e_reducao_de_frutas_vermelhas', 'Juliana (Creme 4 leites e redução de frutas vermelhas)', 10.00, 1),
('ana_brigadeiro_de_limao_siciliano_e_reducao_de_frutas_vermelhas', 'Ana (Brigadeiro de limão siciliano e redução de frutas vermelhas)', 10.00, 1),
('stefan_brigadeiro_de_pistache_e_brigadeiro_de_limao_siciliano', 'Stefan (Brigadeiro de pistache e Brigadeiro de limão siciliano)', 10.00, 1),
('dora_brigadeiro_de_ninho_e_reducao_de_frutas_vermelhas', 'Dora (Brigadeiro de ninho e redução de frutas vermelhas)', 10.00, 1),
('gislaine_brigadeiro_meio_amargo_e_reducao_de_morango', 'Gislaine (Brigadeiro meio-amargo e redução de morango)', 10.00, 1),
('nancy_cocada_cremosa_e_compota_de_abacaxi', 'Nancy (Cocada cremosa e compota de abacaxi)', 10.00, 1),
('priscila_ganache_caramelo_salgado_e_amendoim_tostado', 'Priscila (Ganache, caramelo salgado e amendoim tostado)', 10.00, 1),
('sara_brigadeiro_de_maracuja_e_ganache_meio_amargo', 'Sara (Brigadeiro de maracujá e Ganache meio-amargo)', 10.00, 1),
('tiramissu_creamcheese_frosting_e_nuvem_de_cacau', 'Tiramissu (Creamcheese frosting e nuvem de cacau)', 10.00, 1),
('joao_donato_ganache_meio_amargo_e_cupuacu', 'João Donato (Ganache meio-amargo e cupuaçu)', 10.00, 1);

-- Recheios Exclusivos
INSERT INTO teiko.recheio_exclusivo (recheio_unitario_id1, recheio_unitario_id2, nome, is_ativo) VALUES
(1, 2, 'Creamcheese Frosting com Devil`s Cake (Ganache meio-amargo)', 1),
(1, 3, 'Creamcheese Frosting com Zanza (Ganache meio-amargo e redução de frutas vermelhas)', 1),
(3, 2, 'Zanza (Ganache meio-amargo e redução de frutas vermelhas) com Devil`s Cake (Ganache meio-amargo)', 1);

-- Recheios Pedido
INSERT INTO teiko.recheio_pedido (recheio_unitario_id1, recheio_unitario_id2, recheio_exclusivo, is_ativo) VALUES
(1, 2, NULL, 1),
(NULL, NULL, 1, 1),
(3, 4, NULL, 1),
(5, 6, NULL, 1),
(7, 8, NULL, 1),
(9, 10, NULL, 1),
(1, 3, NULL, 1),
(2, 4, NULL, 1);

-- Bolos com categorias diferentes
INSERT INTO teiko.bolo (recheio_pedido_id, massa_id, cobertura_id, decoracao_id, formato, tamanho, categoria, is_ativo) VALUES
(1, 1, 1, 1, 'CIRCULO', 'TAMANHO_5', 'Carambolo', 1),
(2, 2, 2, 2, 'CORACAO', 'TAMANHO_7', 'Casamento', 1),
(3, 3, 3, 3, 'CIRCULO', 'TAMANHO_12', 'Aniversário', 1),
(4, 1, 2, 4, 'CIRCULO', 'TAMANHO_15', 'Casamento', 1),
(5, 2, 3, 1, 'CORACAO', 'TAMANHO_17', 'Natal', 1),
(6, 3, 1, 2, 'CIRCULO', 'TAMANHO_5', 'Infantil', 1),
(7, 1, 3, 1, 'CIRCULO', 'TAMANHO_7', 'Carambolo', 1),
(8, 2, 1, 2, 'CORACAO', 'TAMANHO_12', 'Festa Junina', 1);

-- Pedidos Bolo
INSERT INTO teiko.pedido_bolo (endereco_id, bolo_id, usuario_id, observacao, data_previsao_entrega, data_ultima_atualizacao, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES
(1, 1, 1, 'Sem cobertura de chocolate', '2024-12-15', NOW(), 'ENTREGA', 'João Silva', '11987654321', 1),
(NULL, 2, 1, 'Com tema de casamento', '2024-12-20', NOW(), 'RETIRADA', 'Maria Oliveira', '11912345678', 1),
(1, 3, NULL, 'Com tema de festa junina', '2024-12-25', NOW(), 'ENTREGA', 'Carlos Souza', '11998765432', 1),
(2, 4, 2, 'Bolo de casamento elegante', '2024-12-18', NOW(), 'ENTREGA', 'Ana Costa', '11987654322', 1),
(3, 5, 3, 'Bolo natalino especial', '2024-12-24', NOW(), 'RETIRADA', 'Pedro Santos', '11987654323', 1),
(1, 6, 1, 'Bolo infantil colorido', '2024-12-22', NOW(), 'ENTREGA', 'Lucia Ferreira', '11987654324', 1),
(NULL, 7, 2, 'Carambolo premium', '2024-12-19', NOW(), 'RETIRADA', 'Roberto Lima', '11987654325', 1),
(2, 8, 3, 'Bolo festa junina', '2024-12-21', NOW(), 'ENTREGA', 'Fernanda Alves', '11987654326', 1);

-- Pedidos Fornada
INSERT INTO teiko.pedido_fornada (fornada_da_vez_id, endereco_id, usuario_id, quantidade, data_previsao_entrega, tipo_entrega, nome_cliente, telefone_cliente, is_ativo) VALUES
(1, 1, 1, 10, '2024-12-11', 'ENTREGA', 'Maria Silva', '11999999999', 1),
(1, 2, 2, 15, '2024-12-11', 'ENTREGA', 'João Souza', '11988888888', 1),
(2, 3, 3, 20, '2024-12-13', 'ENTREGA', 'Ana Oliveira', '11977777777', 1);

-- Resumo Pedidos - Setembro 2024 (atual)
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('PENDENTE', 100.00, '2024-09-15 10:30:00', '2024-09-20', NULL, 1, 1),
('PAGO', 150.00, '2024-09-14 14:20:00', '2024-09-18', NULL, 2, 1),
('CONCLUIDO', 120.00, '2024-09-13 09:15:00', '2024-09-16', NULL, 3, 1),
('PENDENTE', 180.00, '2024-09-12 16:45:00', '2024-09-17', NULL, 4, 1),
('PAGO', 200.00, '2024-09-11 11:30:00', '2024-09-15', NULL, 5, 1),
('CONCLUIDO', 140.00, '2024-09-10 13:20:00', '2024-09-14', NULL, 6, 1),
('PENDENTE', 160.00, '2024-09-09 15:10:00', '2024-09-13', NULL, 7, 1),
('PAGO', 170.00, '2024-09-08 12:45:00', '2024-09-12', NULL, 8, 1),
('PENDENTE', 80.00, '2024-09-07 10:00:00', '2024-09-11', 1, NULL, 1),
('PAGO', 120.00, '2024-09-06 14:30:00', '2024-09-10', 2, NULL, 1),
('CONCLUIDO', 160.00, '2024-09-05 09:45:00', '2024-09-09', 3, NULL, 1);

-- Dados mockados para meses anteriores (Janeiro a Agosto 2024)

-- Janeiro 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 95.00, '2024-01-15 10:30:00', '2024-01-20', NULL, 1, 1),
('CONCLUIDO', 145.00, '2024-01-20 14:20:00', '2024-01-25', NULL, 2, 1),
('CONCLUIDO', 115.00, '2024-01-25 09:15:00', '2024-01-30', NULL, 3, 1),
('CONCLUIDO', 175.00, '2024-01-10 16:45:00', '2024-01-15', NULL, 4, 1),
('CONCLUIDO', 195.00, '2024-01-05 11:30:00', '2024-01-10', NULL, 5, 1),
('CONCLUIDO', 75.00, '2024-01-12 10:00:00', '2024-01-17', 1, NULL, 1),
('CONCLUIDO', 115.00, '2024-01-18 14:30:00', '2024-01-23', 2, NULL, 1);

-- Fevereiro 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 105.00, '2024-02-14 10:30:00', '2024-02-19', NULL, 1, 1),
('CONCLUIDO', 155.00, '2024-02-20 14:20:00', '2024-02-25', NULL, 2, 1),
('CONCLUIDO', 125.00, '2024-02-25 09:15:00', '2024-02-28', NULL, 3, 1),
('CONCLUIDO', 185.00, '2024-02-10 16:45:00', '2024-02-15', NULL, 4, 1),
('CONCLUIDO', 205.00, '2024-02-05 11:30:00', '2024-02-10', NULL, 5, 1),
('CONCLUIDO', 85.00, '2024-02-12 10:00:00', '2024-02-17', 1, NULL, 1),
('CONCLUIDO', 125.00, '2024-02-18 14:30:00', '2024-02-23', 2, NULL, 1);

-- Março 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 110.00, '2024-03-15 10:30:00', '2024-03-20', NULL, 1, 1),
('CONCLUIDO', 160.00, '2024-03-20 14:20:00', '2024-03-25', NULL, 2, 1),
('CONCLUIDO', 130.00, '2024-03-25 09:15:00', '2024-03-30', NULL, 3, 1),
('CONCLUIDO', 190.00, '2024-03-10 16:45:00', '2024-03-15', NULL, 4, 1),
('CONCLUIDO', 210.00, '2024-03-05 11:30:00', '2024-03-10', NULL, 5, 1),
('CONCLUIDO', 90.00, '2024-03-12 10:00:00', '2024-03-17', 1, NULL, 1),
('CONCLUIDO', 130.00, '2024-03-18 14:30:00', '2024-03-23', 2, NULL, 1);

-- Abril 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 115.00, '2024-04-15 10:30:00', '2024-04-20', NULL, 1, 1),
('CONCLUIDO', 165.00, '2024-04-20 14:20:00', '2024-04-25', NULL, 2, 1),
('CONCLUIDO', 135.00, '2024-04-25 09:15:00', '2024-04-30', NULL, 3, 1),
('CONCLUIDO', 195.00, '2024-04-10 16:45:00', '2024-04-15', NULL, 4, 1),
('CONCLUIDO', 215.00, '2024-04-05 11:30:00', '2024-04-10', NULL, 5, 1),
('CONCLUIDO', 95.00, '2024-04-12 10:00:00', '2024-04-17', 1, NULL, 1),
('CONCLUIDO', 135.00, '2024-04-18 14:30:00', '2024-04-23', 2, NULL, 1);

-- Maio 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 120.00, '2024-05-15 10:30:00', '2024-05-20', NULL, 1, 1),
('CONCLUIDO', 170.00, '2024-05-20 14:20:00', '2024-05-25', NULL, 2, 1),
('CONCLUIDO', 140.00, '2024-05-25 09:15:00', '2024-05-30', NULL, 3, 1),
('CONCLUIDO', 200.00, '2024-05-10 16:45:00', '2024-05-15', NULL, 4, 1),
('CONCLUIDO', 220.00, '2024-05-05 11:30:00', '2024-05-10', NULL, 5, 1),
('CONCLUIDO', 100.00, '2024-05-12 10:00:00', '2024-05-17', 1, NULL, 1),
('CONCLUIDO', 140.00, '2024-05-18 14:30:00', '2024-05-23', 2, NULL, 1);

-- Junho 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 125.00, '2024-06-15 10:30:00', '2024-06-20', NULL, 1, 1),
('CONCLUIDO', 175.00, '2024-06-20 14:20:00', '2024-06-25', NULL, 2, 1),
('CONCLUIDO', 145.00, '2024-06-25 09:15:00', '2024-06-30', NULL, 3, 1),
('CONCLUIDO', 205.00, '2024-06-10 16:45:00', '2024-06-15', NULL, 4, 1),
('CONCLUIDO', 225.00, '2024-06-05 11:30:00', '2024-06-10', NULL, 5, 1),
('CONCLUIDO', 105.00, '2024-06-12 10:00:00', '2024-06-17', 1, NULL, 1),
('CONCLUIDO', 145.00, '2024-06-18 14:30:00', '2024-06-23', 2, NULL, 1);

-- Julho 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 130.00, '2024-07-15 10:30:00', '2024-07-20', NULL, 1, 1),
('CONCLUIDO', 180.00, '2024-07-20 14:20:00', '2024-07-25', NULL, 2, 1),
('CONCLUIDO', 150.00, '2024-07-25 09:15:00', '2024-07-30', NULL, 3, 1),
('CONCLUIDO', 210.00, '2024-07-10 16:45:00', '2024-07-15', NULL, 4, 1),
('CONCLUIDO', 230.00, '2024-07-05 11:30:00', '2024-07-10', NULL, 5, 1),
('CONCLUIDO', 110.00, '2024-07-12 10:00:00', '2024-07-17', 1, NULL, 1),
('CONCLUIDO', 150.00, '2024-07-18 14:30:00', '2024-07-23', 2, NULL, 1);

-- Agosto 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 135.00, '2024-08-15 10:30:00', '2024-08-20', NULL, 1, 1),
('CONCLUIDO', 185.00, '2024-08-20 14:20:00', '2024-08-25', NULL, 2, 1),
('CONCLUIDO', 155.00, '2024-08-25 09:15:00', '2024-08-30', NULL, 3, 1),
('CONCLUIDO', 215.00, '2024-08-10 16:45:00', '2024-08-15', NULL, 4, 1),
('CONCLUIDO', 235.00, '2024-08-05 11:30:00', '2024-08-10', NULL, 5, 1),
('CONCLUIDO', 115.00, '2024-08-12 10:00:00', '2024-08-17', 1, NULL, 1),
('CONCLUIDO', 155.00, '2024-08-18 14:30:00', '2024-08-23', 2, NULL, 1);

-- =====================================================
-- DADOS ADICIONAIS MOCKADOS PARA MESES ANTERIORES
-- =====================================================

-- Dados adicionais para Janeiro 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 85.00, '2024-01-08 09:30:00', '2024-01-13', NULL, NULL, 1),
('CONCLUIDO', 125.00, '2024-01-22 15:45:00', '2024-01-27', NULL, NULL, 1),
('CONCLUIDO', 95.00, '2024-01-28 11:20:00', '2024-02-02', NULL, NULL, 1),
('CONCLUIDO', 65.00, '2024-01-14 08:15:00', '2024-01-19', 1, NULL, 1),
('CONCLUIDO', 105.00, '2024-01-30 16:30:00', '2024-02-04', 2, NULL, 1),
('CONCLUIDO', 75.00, '2024-01-03 14:20:00', '2024-01-08', NULL, NULL, 1),
('CONCLUIDO', 110.00, '2024-01-17 10:15:00', '2024-01-22', NULL, NULL, 1),
('CONCLUIDO', 90.00, '2024-01-25 16:45:00', '2024-01-30', NULL, NULL, 1),
('CONCLUIDO', 55.00, '2024-01-11 12:30:00', '2024-01-16', 1, NULL, 1),
('CONCLUIDO', 80.00, '2024-01-29 09:00:00', '2024-02-03', 2, NULL, 1);

-- Dados adicionais para Fevereiro 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 90.00, '2024-02-08 10:15:00', '2024-02-13', NULL, NULL, 1),
('CONCLUIDO', 130.00, '2024-02-22 14:30:00', '2024-02-27', NULL, NULL, 1),
('CONCLUIDO', 100.00, '2024-02-28 12:45:00', '2024-03-05', NULL, NULL, 1),
('CONCLUIDO', 70.00, '2024-02-14 09:00:00', '2024-02-19', 1, NULL, 1),
('CONCLUIDO', 110.00, '2024-02-29 17:20:00', '2024-03-06', 2, NULL, 1),
('CONCLUIDO', 85.00, '2024-02-05 11:30:00', '2024-02-10', NULL, NULL, 1),
('CONCLUIDO', 140.00, '2024-02-18 15:45:00', '2024-02-23', NULL, NULL, 1),
('CONCLUIDO', 95.00, '2024-02-26 13:20:00', '2024-03-03', NULL, NULL, 1),
('CONCLUIDO', 60.00, '2024-02-12 08:30:00', '2024-02-17', 1, NULL, 1),
('CONCLUIDO', 115.00, '2024-02-27 16:15:00', '2024-03-04', 2, NULL, 1);

-- Dados adicionais para Março 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 95.00, '2024-03-08 11:30:00', '2024-03-13', NULL, NULL, 1),
('CONCLUIDO', 135.00, '2024-03-22 15:15:00', '2024-03-27', NULL, NULL, 1),
('CONCLUIDO', 105.00, '2024-03-28 13:20:00', '2024-04-02', NULL, NULL, 1),
('CONCLUIDO', 75.00, '2024-03-14 10:45:00', '2024-03-19', 1, NULL, 1),
('CONCLUIDO', 115.00, '2024-03-30 18:10:00', '2024-04-04', 2, NULL, 1),
('CONCLUIDO', 88.00, '2024-03-06 09:15:00', '2024-03-11', NULL, 1, 1),
('CONCLUIDO', 145.00, '2024-03-20 16:30:00', '2024-03-25', NULL, 2, 1),
('CONCLUIDO', 98.00, '2024-03-29 14:45:00', '2024-04-03', NULL, 3, 1),
('CONCLUIDO', 68.00, '2024-03-16 12:00:00', '2024-03-21', 1, NULL, 1),
('CONCLUIDO', 120.00, '2024-03-31 19:20:00', '2024-04-05', 2, NULL, 1);

-- Dados adicionais para Abril 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 100.00, '2024-04-08 12:45:00', '2024-04-13', NULL, 1, 1),
('CONCLUIDO', 140.00, '2024-04-22 16:00:00', '2024-04-27', NULL, 2, 1),
('CONCLUIDO', 110.00, '2024-04-28 14:15:00', '2024-05-03', NULL, 3, 1),
('CONCLUIDO', 80.00, '2024-04-14 11:30:00', '2024-04-19', 1, NULL, 1),
('CONCLUIDO', 120.00, '2024-04-30 19:20:00', '2024-05-05', 2, NULL, 1),
('CONCLUIDO', 92.00, '2024-04-05 10:20:00', '2024-04-10', NULL, 1, 1),
('CONCLUIDO', 155.00, '2024-04-19 17:15:00', '2024-04-24', NULL, 2, 1),
('CONCLUIDO', 108.00, '2024-04-26 15:30:00', '2024-05-01', NULL, 3, 1),
('CONCLUIDO', 72.00, '2024-04-12 09:45:00', '2024-04-17', 1, NULL, 1),
('CONCLUIDO', 125.00, '2024-04-29 20:10:00', '2024-05-04', 2, NULL, 1);

-- Dados adicionais para Maio 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 105.00, '2024-05-08 13:20:00', '2024-05-13', NULL, 1, 1),
('CONCLUIDO', 145.00, '2024-05-22 16:45:00', '2024-05-27', NULL, 2, 1),
('CONCLUIDO', 115.00, '2024-05-28 15:00:00', '2024-06-02', NULL, 3, 1),
('CONCLUIDO', 85.00, '2024-05-14 12:15:00', '2024-05-19', 1, NULL, 1),
('CONCLUIDO', 125.00, '2024-05-30 20:30:00', '2024-06-04', 2, NULL, 1),
('CONCLUIDO', 98.00, '2024-05-06 11:10:00', '2024-05-11', NULL, 1, 1),
('CONCLUIDO', 150.00, '2024-05-20 18:00:00', '2024-05-25', NULL, 2, 1),
('CONCLUIDO', 112.00, '2024-05-27 16:15:00', '2024-06-01', NULL, 3, 1),
('CONCLUIDO', 78.00, '2024-05-13 10:30:00', '2024-05-18', 1, NULL, 1),
('CONCLUIDO', 130.00, '2024-05-31 21:45:00', '2024-06-05', 2, NULL, 1);

-- Dados adicionais para Junho 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 110.00, '2024-06-08 14:10:00', '2024-06-13', NULL, 1, 1),
('CONCLUIDO', 150.00, '2024-06-22 17:30:00', '2024-06-27', NULL, 2, 1),
('CONCLUIDO', 120.00, '2024-06-28 15:45:00', '2024-07-03', NULL, 3, 1),
('CONCLUIDO', 90.00, '2024-06-14 13:00:00', '2024-06-19', 1, NULL, 1),
('CONCLUIDO', 130.00, '2024-06-30 21:40:00', '2024-07-05', 2, NULL, 1),
('CONCLUIDO', 102.00, '2024-06-07 12:25:00', '2024-06-12', NULL, 1, 1),
('CONCLUIDO', 158.00, '2024-06-21 18:45:00', '2024-06-26', NULL, 2, 1),
('CONCLUIDO', 118.00, '2024-06-29 16:20:00', '2024-07-04', NULL, 3, 1),
('CONCLUIDO', 85.00, '2024-06-15 11:15:00', '2024-06-20', 1, NULL, 1),
('CONCLUIDO', 135.00, '2024-06-30 22:30:00', '2024-07-05', 2, NULL, 1);

-- Dados adicionais para Julho 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 115.00, '2024-07-08 15:00:00', '2024-07-13', NULL, 1, 1),
('CONCLUIDO', 155.00, '2024-07-22 18:15:00', '2024-07-27', NULL, 2, 1),
('CONCLUIDO', 125.00, '2024-07-28 16:30:00', '2024-08-02', NULL, 3, 1),
('CONCLUIDO', 95.00, '2024-07-14 13:45:00', '2024-07-19', 1, NULL, 1),
('CONCLUIDO', 135.00, '2024-07-30 22:50:00', '2024-08-04', 2, NULL, 1),
('CONCLUIDO', 108.00, '2024-07-06 14:20:00', '2024-07-11', NULL, 1, 1),
('CONCLUIDO', 162.00, '2024-07-20 19:30:00', '2024-07-25', NULL, 2, 1),
('CONCLUIDO', 122.00, '2024-07-29 17:15:00', '2024-08-03', NULL, 3, 1),
('CONCLUIDO', 88.00, '2024-07-16 12:30:00', '2024-07-21', 1, NULL, 1),
('CONCLUIDO', 142.00, '2024-07-31 23:15:00', '2024-08-05', 2, NULL, 1);

-- Dados adicionais para Agosto 2024
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
('CONCLUIDO', 120.00, '2024-08-08 15:50:00', '2024-08-13', NULL, 1, 1),
('CONCLUIDO', 160.00, '2024-08-22 19:00:00', '2024-08-27', NULL, 2, 1),
('CONCLUIDO', 130.00, '2024-08-28 17:15:00', '2024-09-02', NULL, 3, 1),
('CONCLUIDO', 100.00, '2024-08-14 14:30:00', '2024-08-19', 1, NULL, 1),
('CONCLUIDO', 140.00, '2024-08-30 23:00:00', '2024-09-04', 2, NULL, 1),
('CONCLUIDO', 112.00, '2024-08-06 16:25:00', '2024-08-11', NULL, 1, 1),
('CONCLUIDO', 168.00, '2024-08-21 20:15:00', '2024-08-26', NULL, 2, 1),
('CONCLUIDO', 128.00, '2024-08-29 18:30:00', '2024-09-03', NULL, 3, 1),
('CONCLUIDO', 95.00, '2024-08-15 13:45:00', '2024-08-20', 1, NULL, 1),
('CONCLUIDO', 145.00, '2024-08-31 23:30:00', '2024-09-05', 2, NULL, 1);

-- ============================================================
-- Bloco de dados VARIADOS para 2025 (Bolo e Fornada)
-- ============================================================
INSERT INTO teiko.resumo_pedido (status, valor, data_pedido, data_entrega, pedido_fornada_id, pedido_bolo_id, is_ativo) VALUES
-- Janeiro 2025
('CONCLUIDO', 220.00, '2025-01-10 12:00:00', '2025-01-15', NULL, 1, 1),
('CANCELADO',  80.00, '2025-01-12 14:00:00', '2025-01-18', NULL, 2, 1),
('CONCLUIDO', 180.00, '2025-01-20 10:00:00', '2025-01-25', 1, NULL, 1),
('PAGO',      160.00, '2025-01-22 09:00:00', '2025-01-27', 2, NULL, 1),
-- Abril 2025
('CONCLUIDO', 195.00, '2025-04-05 09:00:00', '2025-04-10', NULL, 3, 1),
('PAGO',      140.00, '2025-04-07 11:30:00', '2025-04-12', NULL, 4, 1),
('CONCLUIDO', 210.00, '2025-04-15 15:20:00', '2025-04-20', 3, NULL, 1),
('CANCELADO',  90.00, '2025-04-18 18:40:00', '2025-04-23', 4, NULL, 1),
-- Julho 2025
('PAGO',      175.00, '2025-07-03 10:10:00', '2025-07-08', NULL, 5, 1),
('CONCLUIDO', 205.00, '2025-07-10 08:35:00', '2025-07-15', NULL, 6, 1),
('CONCLUIDO', 230.00, '2025-07-18 12:55:00', '2025-07-23', 5, NULL, 1),
('PAGO',      150.00, '2025-07-22 14:25:00', '2025-07-27', 6, NULL, 1),
-- Outubro 2025
('CONCLUIDO', 185.00, '2025-10-02 13:15:00', '2025-10-07', NULL, 7, 1),
('CANCELADO',  95.00, '2025-10-08 16:45:00', '2025-10-13', NULL, 8, 1),
('CONCLUIDO', 240.00, '2025-10-16 09:30:00', '2025-10-21', 7, NULL, 1),
('PAGO',      165.00, '2025-10-20 17:05:00', '2025-10-25', 8, NULL, 1);

-- =====================================================
-- VERIFICAÇÕES FINAIS
-- =====================================================

-- Verificar se todas as tabelas foram criadas
SHOW TABLES;

-- Verificar se a coluna categoria existe na tabela decoracao
DESCRIBE teiko.decoracao;

-- Verificar dados inseridos
SELECT COUNT(*) as total_usuarios FROM teiko.usuario;
SELECT COUNT(*) as total_produtos_fornada FROM teiko.produto_fornada;
SELECT COUNT(*) as total_imagens_produtos FROM teiko.imagem_produto_fornada;
SELECT COUNT(*) as total_decoracoes FROM teiko.decoracao;
SELECT COUNT(*) as total_imagens_decoracoes FROM teiko.imagem_decoracao;
SELECT COUNT(*) as total_bolos FROM teiko.bolo;


-- insere adicionais para decoração

DESC teiko.adicional;
INSERT INTO teiko.adicional (descricao, is_ativo) VALUES
('Disco ball', 1),
('Desenho', 1),
('Pérolas na finalização', 1),
('Metalizado (Prata ou Dourado)', 1),
('Glitter', 1),
('Cereja (Com ou sem glitter)', 1),
('Laços', 1),
('Escrita', 1),
('Borda (Topo e Base)', 1),
('Lacinhos', 1);

select * from adicional;

DESC teiko.adicional_decoracao;
INSERT INTO teiko.adicional_decoracao (decoracao_id, adicional_id) VAlUES
-- VINTAGE
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),

-- FLORAL
(2, 1),
(2, 3),
(2, 5),
(2, 8),
(2, 9),

-- MY CARAMBOLO
(3, 9),
(3, 10),

-- SHAG CAKE
(4, 5);

-- =====================================================
-- SCRIPT FINALIZADO COM SUCESSO
-- =====================================================

SELECT * FROM teiko.usuario;
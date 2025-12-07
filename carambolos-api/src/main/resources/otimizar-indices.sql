-- Script de otimização para acelerar a query de listagem de bolos
-- Execute este script no banco RDS para melhorar a performance

USE teiko;

-- Índices para tabela bolo (principal)
-- Nota: Se o índice já existir, ignore o erro
CREATE INDEX idx_bolo_massa_id ON bolo(massa_id);
CREATE INDEX idx_bolo_recheio_pedido_id ON bolo(recheio_pedido_id);
CREATE INDEX idx_bolo_cobertura_id ON bolo(cobertura_id);
CREATE INDEX idx_bolo_decoracao_id ON bolo(decoracao_id);
CREATE INDEX idx_bolo_categoria_ativo ON bolo(categoria, is_ativo);

-- Índices para tabela recheio_pedido
CREATE INDEX idx_recheio_pedido_unitario1 ON recheio_pedido(recheio_unitario_id1);
CREATE INDEX idx_recheio_pedido_unitario2 ON recheio_pedido(recheio_unitario_id2);
CREATE INDEX idx_recheio_pedido_exclusivo ON recheio_pedido(recheio_exclusivo);

-- Índices para tabela recheio_exclusivo
CREATE INDEX idx_recheio_exclusivo_unitario1 ON recheio_exclusivo(recheio_unitario_id1);
CREATE INDEX idx_recheio_exclusivo_unitario2 ON recheio_exclusivo(recheio_unitario_id2);

-- Verificar índices criados
SELECT 
    TABLE_NAME,
    INDEX_NAME,
    COLUMN_NAME
FROM 
    INFORMATION_SCHEMA.STATISTICS
WHERE 
    TABLE_SCHEMA = 'teiko'
    AND TABLE_NAME IN ('bolo', 'recheio_pedido', 'recheio_exclusivo')
ORDER BY 
    TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;


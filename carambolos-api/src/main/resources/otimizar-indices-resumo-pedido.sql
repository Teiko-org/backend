-- Script de otimização para acelerar queries de resumo_pedido
-- Execute este script no banco RDS para melhorar a performance

USE teiko;

-- Índices para tabela resumo_pedido (CRÍTICO para performance)
-- Nota: Se o índice já existir, ignore o erro
CREATE INDEX idx_resumo_pedido_is_ativo ON resumo_pedido(is_ativo);
CREATE INDEX idx_resumo_pedido_status ON resumo_pedido(status);
CREATE INDEX idx_resumo_pedido_status_ativo ON resumo_pedido(status, is_ativo);
CREATE INDEX idx_resumo_pedido_pedido_bolo_id_ativo ON resumo_pedido(pedido_bolo_id, is_ativo);
CREATE INDEX idx_resumo_pedido_pedido_fornada_id_ativo ON resumo_pedido(pedido_fornada_id, is_ativo);
CREATE INDEX idx_resumo_pedido_data_pedido_ativo ON resumo_pedido(data_pedido, is_ativo);

-- Índices para tabela pedido_bolo
CREATE INDEX idx_pedido_bolo_is_ativo ON pedido_bolo(is_ativo);
CREATE INDEX idx_pedido_bolo_bolo_id_ativo ON pedido_bolo(bolo_id, is_ativo);

-- Verificar índices criados
SELECT 
    TABLE_NAME,
    INDEX_NAME,
    COLUMN_NAME
FROM 
    INFORMATION_SCHEMA.STATISTICS
WHERE 
    TABLE_SCHEMA = 'teiko'
    AND TABLE_NAME IN ('resumo_pedido', 'pedido_bolo')
ORDER BY 
    TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;


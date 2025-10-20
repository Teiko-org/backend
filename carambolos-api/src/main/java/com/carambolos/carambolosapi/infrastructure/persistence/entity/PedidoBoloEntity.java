package com.carambolos.carambolosapi.infrastructure.persistence.entity;

import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;
import com.carambolos.carambolosapi.system.security.CryptoAttributeConverter;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "pedido_bolo")
public class PedidoBoloEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "endereco_id")
    private Integer enderecoId;

    @Column(name = "bolo_id")
    private Integer boloId;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    private String observacao;

    @Column(name = "data_previsao_entrega")
    private LocalDate dataPrevisaoEntrega;

    @Column(name = "data_ultima_atualizacao")
    private LocalDateTime dataUltimaAtualizacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_entrega")
    private TipoEntregaEnum tipoEntrega;

    @Column(name = "nome_cliente")
    @Convert(converter = CryptoAttributeConverter.class)
    private String nomeCliente;

    @Column(name = "telefone_cliente")
    @Convert(converter = CryptoAttributeConverter.class)
    private String telefoneCliente;

    @Column(name = "horario_retirada")
    private String horarioRetirada;

    @Column(name = "is_ativo")
    private Boolean isAtivo = true;

    public PedidoBoloEntity() {
    }

    public PedidoBoloEntity(Integer id,
                            Integer enderecoId,
                            Integer boloId,
                            Integer usuarioId,
                            String observacao,
                            LocalDate dataPrevisaoEntrega,
                            LocalDateTime dataUltimaAtualizacao,
                            TipoEntregaEnum tipoEntrega,
                            String nomeCliente,
                            String telefoneCliente,
                            String horarioRetirada,
                            Boolean isAtivo
    ) {
        this.id = id;
        this.enderecoId = enderecoId;
        this.boloId = boloId;
        this.usuarioId = usuarioId;
        this.observacao = observacao;
        this.dataPrevisaoEntrega = dataPrevisaoEntrega;
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
        this.tipoEntrega = tipoEntrega;
        this.nomeCliente = nomeCliente;
        this.telefoneCliente = telefoneCliente;
        this.horarioRetirada = horarioRetirada;
        this.isAtivo = isAtivo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEnderecoId() {
        return enderecoId;
    }

    public void setEnderecoId(Integer enderecoId) {
        this.enderecoId = enderecoId;
    }

    public Integer getBoloId() {
        return boloId;
    }

    public void setBoloId(Integer boloId) {
        this.boloId = boloId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDate getDataPrevisaoEntrega() {
        return dataPrevisaoEntrega;
    }

    public void setDataPrevisaoEntrega(LocalDate dataPrevisaoEntrega) {
        this.dataPrevisaoEntrega = dataPrevisaoEntrega;
    }

    public LocalDateTime getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public TipoEntregaEnum getTipoEntrega() {
        return tipoEntrega;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getTelefoneCliente() {
        return telefoneCliente;
    }

    public void setTelefoneCliente(String telefoneCliente) {
        this.telefoneCliente = telefoneCliente;
    }

    public String getHorarioRetirada() {
        return horarioRetirada;
    }

    public void setHorarioRetirada(String horarioRetirada) {
        this.horarioRetirada = horarioRetirada;
    }

    public void setTipoEntrega(TipoEntregaEnum tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}

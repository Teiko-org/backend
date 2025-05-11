//package com.carambolos.carambolosapi.model;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import jakarta.persistence.*;
//
//@Entity
//public class Decoracao {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Schema(description = "Identificador único da decoração", example = "1")
//    private Integer id;
//
//    @Lob
//    @Column(name = "imagem_referencia")
//    @Schema(description = "Imagem da decoração", example = "imagem.png")
//    private byte[] imagemReferencia;
//
//    @Column(name = "estilo_decoracao")
//    @Schema(description = "Observações da decoração", example = "Estilo cyberpunk")
//    private String estiloDecoracao;
//
////    @Column(name = "is_ativo")
////    @Schema(description = "Indica se a decoração está ativa", example = "true")
////    private Boolean isAtivo = true;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public byte[] getImagemReferencia() {
//        return imagemReferencia;
//    }
//
//    public void setImagemReferencia(byte[] imagemReferencia) {
//        this.imagemReferencia = imagemReferencia;
//    }
//
//
//    public String getEstiloDecoracao() {
//        return estiloDecoracao;
//    }
//
//    public void setEstiloDecoracao(String estiloDecoracao) {
//        this.estiloDecoracao = estiloDecoracao;
//    }
//
////    public Boolean getAtivo() {
////        return isAtivo;
////    }
////
////    public void setAtivo(Boolean ativo) {
////        isAtivo = ativo;
////    }
//}

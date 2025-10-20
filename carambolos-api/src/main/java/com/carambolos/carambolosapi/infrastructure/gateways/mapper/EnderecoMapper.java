package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.Endereco;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.EnderecoEntity;
import com.carambolos.carambolosapi.infrastructure.web.request.EnderecoRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.EnderecoResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public class EnderecoMapper {
    public EnderecoEntity toEntity(Endereco enderecoDomain) {
        return new EnderecoEntity(
                enderecoDomain.getId(),
                enderecoDomain.getNome(),
                enderecoDomain.getCep(),
                enderecoDomain.getEstado(),
                enderecoDomain.getCidade(),
                enderecoDomain.getBairro(),
                enderecoDomain.getLogradouro(),
                enderecoDomain.getNumero(),
                enderecoDomain.getComplemento(),
                enderecoDomain.getReferencia(),
                enderecoDomain.isAtivo(),
                enderecoDomain.getUsuario(),
                enderecoDomain.getDedupHash()
        );
    }

    public Endereco toDomain(EnderecoEntity enderecoEntity) {
        return new Endereco(
                enderecoEntity.getId(),
                enderecoEntity.getNome(),
                enderecoEntity.getCep(),
                enderecoEntity.getEstado(),
                enderecoEntity.getCidade(),
                enderecoEntity.getBairro(),
                enderecoEntity.getLogradouro(),
                enderecoEntity.getNumero(),
                enderecoEntity.getComplemento(),
                enderecoEntity.getReferencia(),
                enderecoEntity.isAtivo(),
                enderecoEntity.getUsuario(),
                enderecoEntity.getDedupHash()
        );
    }

    public List<Endereco> toDomain(List<EnderecoEntity> enderecoEntity) {
        return enderecoEntity.stream().map(this::toDomain).toList();
    }

    public Page<Endereco> toDomain(Page<EnderecoEntity> page) {
        return page.map(this::toDomain);
    }

    public static EnderecoResponseDTO toResponseDTO(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        EnderecoResponseDTO responseDto = new EnderecoResponseDTO();
        responseDto.setId(endereco.getId());
        responseDto.setCep(endereco.getCep());
        responseDto.setNome(endereco.getNome());
        responseDto.setEstado(endereco.getEstado());
        responseDto.setCidade(endereco.getCidade());
        responseDto.setBairro(endereco.getBairro());
        responseDto.setLogradouro(endereco.getLogradouro());
        responseDto.setNumero(endereco.getNumero());
        responseDto.setComplemento(endereco.getComplemento());
        responseDto.setReferencia(endereco.getReferencia());
        responseDto.setAtivo(endereco.isAtivo());
        responseDto.setUsuario(endereco.getUsuario());

        return responseDto;
    }

    public static Endereco toDomain(EnderecoRequestDTO requestDto) {
        if (requestDto == null) {
            return null;
        }

        Endereco endereco = new Endereco();
        endereco.setCep(requestDto.getCep());
        endereco.setNome(requestDto.getNome());
        endereco.setEstado(requestDto.getEstado());
        endereco.setCidade(requestDto.getCidade());
        endereco.setBairro(requestDto.getBairro());
        endereco.setLogradouro(requestDto.getLogradouro());
        endereco.setNumero(requestDto.getNumero());
        endereco.setComplemento(requestDto.getComplemento());
        endereco.setReferencia(requestDto.getReferencia());
        endereco.setUsuario(requestDto.getUsuario());

        return endereco;
    }
}

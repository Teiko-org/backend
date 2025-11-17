package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.DecoracaoEntity;
import com.carambolos.carambolosapi.infrastructure.web.request.DecoracaoRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.DecoracaoResponseDTO;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.DecoracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DecoracaoService {

    @Autowired
    private DecoracaoRepository decoracaoRepository;

    @Autowired
    private AzureStorageService azureStorageService;







    public DecoracaoResponseDTO buscarPorId(Integer id) {
        DecoracaoEntity decoracaoEntity = decoracaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Decoração não encontrada"));
        return DecoracaoResponseDTO.fromEntity(decoracaoEntity);
    }

    public void desativar(Integer id) {
        DecoracaoEntity decoracaoEntity = decoracaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Decoração não encontrada"));
        decoracaoEntity.setIsAtivo(false);
        decoracaoRepository.save(decoracaoEntity);
    }

    public void reativar(Integer id) {
        DecoracaoEntity decoracaoEntity = decoracaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Decoração não encontrada"));
        decoracaoEntity.setIsAtivo(true);
        decoracaoRepository.save(decoracaoEntity);
    }

    public DecoracaoResponseDTO atualizar(Integer id, DecoracaoRequestDTO request) {
        DecoracaoEntity decoracaoEntity = decoracaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Decoração não encontrada"));

        decoracaoEntity.setObservacao(request.observacao());
        decoracaoEntity.setCategoria(request.categoria());

        return DecoracaoResponseDTO.fromEntity(decoracaoRepository.save(decoracaoEntity));
    }
}

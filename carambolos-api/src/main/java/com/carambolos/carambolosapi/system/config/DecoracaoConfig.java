package com.carambolos.carambolosapi.system.config;

import com.azure.storage.blob.BlobContainerClient;
import com.carambolos.carambolosapi.application.gateways.AdicionalDecoracaoGateway;
import com.carambolos.carambolosapi.application.gateways.DecoracaoGateway;
import com.carambolos.carambolosapi.application.gateways.StorageGateway;
import com.carambolos.carambolosapi.application.usecases.AdicionalDecoracaoUseCase;
import com.carambolos.carambolosapi.application.usecases.DecoracaoUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.AdicionalDecoracaoGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.AzureStorageGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.DecoracaoGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.AdicionalDecoracaoMapper;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.DecoracaoMapper;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.ImagemDecoracaoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.AdicionalDecoracaoRepository;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.DecoracaoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class DecoracaoConfig {
    @Bean
    public DecoracaoGateway createDecoracaoGateway(DecoracaoRepository repository, DecoracaoMapper mapper) {
        return new DecoracaoGatewayImpl(repository, mapper);
    }

    @Bean
    public DecoracaoUseCase createDecoracaoUseCase(
            DecoracaoGateway decoracaoGateway,
            AdicionalDecoracaoGateway adicionalDecoracaoGateway,
            StorageGateway storageGateway
    ) {
        return new DecoracaoUseCase(decoracaoGateway, adicionalDecoracaoGateway, storageGateway);
    }

    @Bean
    public ImagemDecoracaoMapper createImagemMapper(@Lazy DecoracaoMapper decoracaoMapper) {
        return new ImagemDecoracaoMapper(decoracaoMapper);
    }

    @Bean
    public DecoracaoMapper createDecoracaoMapper(ImagemDecoracaoMapper imagemDecoracaoMapper) {
        return new DecoracaoMapper(imagemDecoracaoMapper);
    }

    @Bean
    public AdicionalDecoracaoUseCase createAdicionalDecoracaoUseCase(
            AdicionalDecoracaoGateway adicionalDecoracaoGateway
    ) {
        return new AdicionalDecoracaoUseCase(adicionalDecoracaoGateway);
    }

    @Bean
    public AdicionalDecoracaoGateway createAdicionalDecoracaoGateway(
            AdicionalDecoracaoRepository repository,
            AdicionalDecoracaoMapper mapper
    ) {
        return new AdicionalDecoracaoGatewayImpl(repository, mapper);
    }

    @Bean
    public AdicionalDecoracaoMapper createAdicionalDecoracaoMapper() {
        return new AdicionalDecoracaoMapper();
    }
}

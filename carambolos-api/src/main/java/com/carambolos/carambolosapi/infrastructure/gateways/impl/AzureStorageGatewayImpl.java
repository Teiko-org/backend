package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.carambolos.carambolosapi.application.gateways.StorageGateway;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public class AzureStorageGatewayImpl implements StorageGateway {
    private final BlobContainerClient containerClient;

    public AzureStorageGatewayImpl(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    @Override
    public String upload(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            BlobClient blobClient = containerClient.getBlobClient(fileName);
            try (InputStream is = file.getInputStream()) {
                blobClient.upload(is, file.getSize(), true);
            }
            return blobClient.getBlobUrl();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload para o Azure Blob Storage", e);
        }
    }
}

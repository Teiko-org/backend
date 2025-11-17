package com.carambolos.carambolosapi.application.usecases;

import com.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class AzureStorageService {

    private final BlobContainerClient containerClient;

    public AzureStorageService(@Value("${azure.storage.connection-string}") String connectionString,
                               @Value("${azure.storage.container-name}") String containerName) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        this.containerClient = blobServiceClient.getBlobContainerClient(containerName);
    }
}

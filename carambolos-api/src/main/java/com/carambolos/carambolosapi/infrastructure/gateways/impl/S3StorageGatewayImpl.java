package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.gateways.StorageGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.util.UUID;
import java.util.UUID;

@Component
public class S3StorageGatewayImpl implements StorageGateway {

    private static final Logger logger = LoggerFactory.getLogger(S3StorageGatewayImpl.class);

    private final S3Client s3Client;
    private final String bucketName;
    private final Region region;

    public S3StorageGatewayImpl(
            @Value("${aws.s3.bucket-name}") String bucketName,
            @Value("${aws.region}") String region
    ) {
        this.bucketName = bucketName;
        this.region = Region.of(region);
        this.s3Client = S3Client.builder()
                .region(this.region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Override
    public String upload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
            String sanitizedFilename = originalFilename.replace(" ", "_");
            String key = UUID.randomUUID() + "_" + sanitizedFilename;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            try (InputStream is = file.getInputStream()) {
                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(is, file.getSize()));
            }

            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region.id(), key);
        } catch (Exception e) {
            logger.error("Erro ao fazer upload para o AWS S3. bucket={}, region={}, fileName={}, contentType={}",
                    bucketName,
                    region,
                    file != null ? file.getOriginalFilename() : "null",
                    file != null ? file.getContentType() : "null",
                    e);
            throw new RuntimeException("Erro ao fazer upload para o AWS S3: " + e.getMessage(), e);
        }
    }
}

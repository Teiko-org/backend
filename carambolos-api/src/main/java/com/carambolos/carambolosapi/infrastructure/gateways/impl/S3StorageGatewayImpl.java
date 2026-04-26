package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.gateways.StorageGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class S3StorageGatewayImpl implements StorageGateway {

    private static final Logger logger = LoggerFactory.getLogger(S3StorageGatewayImpl.class);

    @Override
    public String upload(MultipartFile file) {
        logger.info("Storage upload disabled — returning empty URL for file: {}",
                file != null ? file.getOriginalFilename() : "null");
        return "";
    }
}

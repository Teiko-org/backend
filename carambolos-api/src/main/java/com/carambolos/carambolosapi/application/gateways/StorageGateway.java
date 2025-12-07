package com.carambolos.carambolosapi.application.gateways;

import org.springframework.web.multipart.MultipartFile;

public interface StorageGateway {
    String upload(MultipartFile file);
}

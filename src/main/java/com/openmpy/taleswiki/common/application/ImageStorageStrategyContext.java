package com.openmpy.taleswiki.common.application;

import com.openmpy.taleswiki.common.presentation.response.ImageUploadResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageStorageStrategyContext {

    private final Map<String, ImageService> serviceMap;

    public ImageStorageStrategyContext(final List<ImageService> services) {
        this.serviceMap = services.stream().collect(Collectors.toMap(ImageService::getStorageType, s -> s));
    }

    public ImageUploadResponse upload(final String storage, final MultipartFile file) {
        final ImageService imageService = serviceMap.get(storage.toUpperCase());

        if (imageService == null) {
            throw new IllegalArgumentException("찾을 수 없는 이미지 서비스입니다. " + storage);
        }
        return imageService.upload(file);
    }
}

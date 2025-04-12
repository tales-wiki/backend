package com.openmpy.taleswiki.common.application;

import com.openmpy.taleswiki.common.presentation.response.ImageUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    ImageUploadResponse upload(final MultipartFile file);

    String getStorageType();
}

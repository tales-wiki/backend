package com.openmpy.taleswiki.common.application;

import com.openmpy.taleswiki.common.presentation.response.ImageUploadResponse;
import com.openmpy.taleswiki.common.properties.ImageProperties;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageProperties imageProperties;

    public ImageUploadResponse upload(final MultipartFile file) {
        try {
            final String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            final Path uploadPath = Paths.get(imageProperties.dir());

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            final Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return new ImageUploadResponse(fileName);
        } catch (final IOException e) {
            throw new IllegalArgumentException("파일 업로드 중 오류 발생", e);
        }
    }
}

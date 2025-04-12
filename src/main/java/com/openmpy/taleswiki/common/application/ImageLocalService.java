package com.openmpy.taleswiki.common.application;

import static com.openmpy.taleswiki.common.util.FileLoaderUtil.getExtension;

import com.openmpy.taleswiki.common.presentation.response.ImageUploadResponse;
import com.openmpy.taleswiki.common.util.FileLoaderUtil;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageLocalService implements ImageService {

    private static final String IMAGES_DIR = System.getProperty("user.home") + "/tales-wiki/images";
    private static final String LOCAL_STORAGE_TYPE = "LOCAL";

    @PostConstruct
    public void initDirectory() {
        final File uploadDir = new File(IMAGES_DIR);

        if (!uploadDir.exists()) {
            final boolean isCreated = uploadDir.mkdirs();

            if (!isCreated) {
                throw new IllegalStateException("이미지 디렉토리 생성 실패");
            }
        }
    }

    @Override
    public ImageUploadResponse upload(final MultipartFile file) {
        final String extension = getExtension(file);
        FileLoaderUtil.getExtension(file);

        try {
            final String fileName = UUID.randomUUID() + "." + extension;
            final Path uploadPath = Paths.get(IMAGES_DIR);
            final Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return new ImageUploadResponse(fileName);
        } catch (final IOException e) {
            throw new IllegalArgumentException("파일 업로드 중 오류 발생", e);
        }
    }

    @Override
    public String getStorageType() {
        return LOCAL_STORAGE_TYPE;
    }
}

package com.openmpy.taleswiki.common.presentation;

import com.openmpy.taleswiki.common.application.ImageStorageStrategyContext;
import com.openmpy.taleswiki.common.presentation.response.ImageUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/images")
@RestController
public class ImageController {

    private static final String IMAGE_DIR = System.getProperty("user.home") + "/tales-wiki/images/";

    private final ImageStorageStrategyContext imageStorageStrategyContext;

    @PostMapping("/upload")
    public ResponseEntity<ImageUploadResponse> upload(
            @RequestParam("image") final MultipartFile file,
            @RequestParam(value = "storage", required = false, defaultValue = "local") final String storage
    ) {
        final ImageUploadResponse response = imageStorageStrategyContext.upload(storage, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{imageName}")
    public Resource getImage(@PathVariable final String imageName) {
        return new FileSystemResource(IMAGE_DIR + imageName);
    }
}

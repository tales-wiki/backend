package com.openmpy.taleswiki.common.presentation;

import com.openmpy.taleswiki.common.application.ImageService;
import com.openmpy.taleswiki.common.presentation.response.ImageUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/images")
@RestController
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageUploadResponse> upload(@RequestParam("image") final MultipartFile file) {
        final ImageUploadResponse response = imageService.upload(file);
        return ResponseEntity.ok(response);
    }
}

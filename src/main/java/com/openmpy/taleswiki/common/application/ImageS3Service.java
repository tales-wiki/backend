package com.openmpy.taleswiki.common.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_IMAGE_FILE_EXTENSION;
import static com.openmpy.taleswiki.common.util.FileLoaderUtil.getExtension;

import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.common.presentation.response.ImageUploadResponse;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@RequiredArgsConstructor
@Service
public class ImageS3Service {

    private static final String BUCKET_NAME = "tales-wiki-bucket";
    private static final String BUCKET_ALREADY_OWNED_BY_YOU = "BucketAlreadyOwnedByYou";
    private static final Set<String> VALID_FILE_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif", "bmp", "webp", "tiff", "svg"
    );

    private final S3Client s3Client;

    @PostConstruct
    public void initBucket() {
        try {
            final CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(BUCKET_NAME)
                    .build();

            s3Client.createBucket(bucketRequest);
        } catch (final S3Exception e) {
            if (!e.awsErrorDetails().errorCode().equals(BUCKET_ALREADY_OWNED_BY_YOU)) {
                throw e;
            }
        }
    }

    public ImageUploadResponse upload(final MultipartFile file) {
        final String extension = getExtension(file);
        validateFileExtension(extension);

        try {
            final String key = UUID.randomUUID() + "." + extension;
            final PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(objectRequest, RequestBody.fromBytes(file.getBytes()));
            return new ImageUploadResponse(key);
        } catch (final IOException e) {
            throw new IllegalArgumentException("파일 업로드 중 오류 발생", e);
        }
    }

    private void validateFileExtension(final String extension) {
        if (!VALID_FILE_EXTENSIONS.contains(extension)) {
            throw new CustomException(INVALID_IMAGE_FILE_EXTENSION);
        }
    }
}

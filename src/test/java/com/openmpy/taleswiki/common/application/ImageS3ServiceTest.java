package com.openmpy.taleswiki.common.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_IMAGE_FILE_EXTENSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.common.presentation.response.ImageUploadResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ImageS3ServiceTest {

    @MockitoBean
    private ImageS3Service imageS3Service;

    @DisplayName("[통과] 이미지 파일을 업로드한다.")
    @Test
    void image_s3_service_test_01() {
        // given
        final MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[10]);
        final ImageUploadResponse response = new ImageUploadResponse("uuid-test.jpg");

        // when
        when(imageS3Service.upload(file)).thenReturn(response);

        // then
        assertThat(response.url()).isEqualTo("uuid-test.jpg");
    }

    @DisplayName("[예외] 이미지 파일이 아니다.")
    @Test
    void 예외_image_s3_service_test_01() {
        // given
        final MockMultipartFile file = new MockMultipartFile("image", "test.pdf", "application/pdf", new byte[10]);

        when(imageS3Service.upload(file)).thenThrow(new CustomException(INVALID_IMAGE_FILE_EXTENSION));

        // when & then
        assertThatThrownBy(() -> imageS3Service.upload(file))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_IMAGE_FILE_EXTENSION.getMessage());
    }
}
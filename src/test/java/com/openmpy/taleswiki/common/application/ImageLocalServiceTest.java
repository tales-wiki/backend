package com.openmpy.taleswiki.common.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.openmpy.taleswiki.common.presentation.response.ImageUploadResponse;
import com.openmpy.taleswiki.support.ServiceTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class ImageLocalServiceTest extends ServiceTestSupport {

    @MockitoBean
    private ImageLocalService imageLocalService;

    @DisplayName("[통과] 이미지 파일을 업로드한다.")
    @Test
    void image_local_service_test_01() {
        // given
        final MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[10]);
        final ImageUploadResponse response = new ImageUploadResponse("uuid-test.jpg");

        // when
        when(imageLocalService.upload(file)).thenReturn(response);

        // then
        assertThat(response.url()).isEqualTo("uuid-test.jpg");
    }
}
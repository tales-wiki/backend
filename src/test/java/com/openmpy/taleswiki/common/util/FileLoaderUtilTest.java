package com.openmpy.taleswiki.common.util;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class FileLoaderUtilTest {

    @DisplayName("[통과] 파일 내용을 읽어서 String 값으로 반환한다.")
    @Test
    void file_loader_util_test_01() {
        // given
        final String fileName = "sample.md";

        // when
        final String content = FileLoaderUtil.loadMarkdownFile(fileName);

        // then
        assertThat(content).isNotNull();
        assertThat(content).contains("샘플");
    }

    @DisplayName("[통과] 파일명에서 확장자를 추출한다.")
    @Test
    void file_loader_util_test_02() {
        // given
        final MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[10]);

        // when
        final String extension = FileLoaderUtil.getExtension(file);

        // then
        assertThat(extension).isEqualTo("jpg");
    }

    @DisplayName("[통과] 파일명이 null 또는 .이 포함되지 않을 경우 빈 값을 반환한다.")
    @Test
    void file_loader_util_test_03() {
        // given
        final MockMultipartFile file = new MockMultipartFile("image", "test", "image/jpeg", new byte[10]);

        // when
        final String extension = FileLoaderUtil.getExtension(file);

        // then
        assertThat(extension).isEmpty();
    }

    @DisplayName("[예외] 읽을 파일 경로를 찾지 못한다.")
    @Test
    void 예외_file_loader_util_test_01() {
        // when & then
        assertThatThrownBy(() -> FileLoaderUtil.loadMarkdownFile("test.md"))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_FOUND_FILE.getMessage() + " " + "test.md");
    }
}
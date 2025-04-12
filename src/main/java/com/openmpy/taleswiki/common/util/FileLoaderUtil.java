package com.openmpy.taleswiki.common.util;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_IMAGE_FILE_EXTENSION;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_FILE;

import com.openmpy.taleswiki.common.exception.CustomException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

public class FileLoaderUtil {

    private static final String MARK_DOWN_PATH = "markdown/";
    private static final Set<String> VALID_FILE_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif", "bmp", "webp", "tiff", "svg"
    );

    private FileLoaderUtil() {
        throw new IllegalStateException("유틸리티 클래스입니다.");
    }

    public static String loadMarkdownFile(final String fileName) {
        final ClassLoader classLoader = FileLoaderUtil.class.getClassLoader();

        try (final InputStream inputStream = classLoader.getResourceAsStream(MARK_DOWN_PATH + fileName)) {
            if (inputStream == null) {
                throw new CustomException(NOT_FOUND_FILE, fileName);
            }

            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (final CustomException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getExtension(final MultipartFile file) {
        final String filename = file.getOriginalFilename();
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public void validateFileExtension(final String extension) {
        if (!VALID_FILE_EXTENSIONS.contains(extension)) {
            throw new CustomException(INVALID_IMAGE_FILE_EXTENSION);
        }
    }
}

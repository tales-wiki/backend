package com.openmpy.taleswiki.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class FileLoaderUtil {

    private static final String MARK_DOWN_PATH = "markdown/";

    private FileLoaderUtil() {
        throw new IllegalStateException("유틸리티 클래스입니다.");
    }

    public static String loadMarkdownFile(final String fileName) {
        final ClassLoader classLoader = FileLoaderUtil.class.getClassLoader();

        try (final InputStream inputStream = classLoader.getResourceAsStream(MARK_DOWN_PATH + fileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + fileName);
            }
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

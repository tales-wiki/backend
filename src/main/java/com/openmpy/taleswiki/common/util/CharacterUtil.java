package com.openmpy.taleswiki.common.util;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_TEXT_NULL_AND_BLANK;

import com.openmpy.taleswiki.common.exception.CustomException;
import java.util.Map;

public class CharacterUtil {

    private static final char[] CHO_SUNG = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ',
            'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ',
            'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };
    private static final Map<Character, Character> CHO_SUNG_NORMALIZATION = Map.of(
            'ㄲ', 'ㄱ',
            'ㄸ', 'ㄷ',
            'ㅃ', 'ㅂ',
            'ㅆ', 'ㅅ',
            'ㅉ', 'ㅈ'
    );

    private CharacterUtil() {
        throw new IllegalStateException("유틸리티 클래스입니다.");
    }

    public static Character getInitialGroup(final String text) {
        if (text == null || text.isEmpty()) {
            throw new CustomException(NOT_ALLOWED_TEXT_NULL_AND_BLANK);
        }

        final char c = text.charAt(0);

        if (c >= '가' && c <= '힣') {
            final int uniVal = c - '가';
            final int choIndex = uniVal / (21 * 28);
            char cho = CHO_SUNG[choIndex];

            // 된소리면 일반 초성으로 매핑
            cho = CHO_SUNG_NORMALIZATION.getOrDefault(cho, cho);
            return cho;
        }
        if (Character.isAlphabetic(c)) {
            return Character.toUpperCase(c);
        }
        return c;
    }
}

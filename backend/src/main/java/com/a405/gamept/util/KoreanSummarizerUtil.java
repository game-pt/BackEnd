package com.a405.gamept.util;

import com.github.tuguri8.lib.KoreanSummarizer;

public class KoreanSummarizerUtil {

    private static final KoreanSummarizer koreanSummarizer = new KoreanSummarizer();

    public static String summarize(String text) {
        return koreanSummarizer.summarize(text);
    }

}

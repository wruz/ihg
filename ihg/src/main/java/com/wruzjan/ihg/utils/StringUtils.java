package com.wruzjan.ihg.utils;

import java.util.Locale;

import androidx.annotation.NonNull;

public class StringUtils {

    private StringUtils() {
        // no-op
    }

    @NonNull
    public static String formatFloatOneDecimal(float value) {
        return String.format(Locale.US, "%.1f", value);
    }
}

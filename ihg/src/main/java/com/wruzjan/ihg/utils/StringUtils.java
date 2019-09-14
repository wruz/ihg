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

    @NonNull
    public static int[] parseNumberArrayFromString(@NonNull String numberArrayString) {
        String[] indicesStringArray = numberArrayString.split(",");
        int[] indices = new int[indicesStringArray.length];
        for (int i = 0; i < indicesStringArray.length; i++) {
            indices[i] = Integer.parseInt(indicesStringArray[i]);
        }
        return indices;
    }
}

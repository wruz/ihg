package com.wruzjan.ihg.utils;

import androidx.annotation.NonNull;

public class ArrayUtils {

    public static int indexOf(@NonNull String[] stringArray, @NonNull String stringToFind) {
        for (int i = 0; i < stringArray.length; i++) {
            if (stringArray[i].equalsIgnoreCase(stringToFind)) {
                return i;
            }
        }
        return -1;
    }
}

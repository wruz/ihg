package com.wruzjan.ihg.utils;

import java.util.Locale;

import androidx.annotation.NonNull;

public class StringUtils {

    private static final String MAC_ADDRESS_IEEE_802_FORMAT_REGEX = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";

    private StringUtils() {
        // no-op
    }

    @NonNull
    public static String formatFloatOneDecimal(float value) {
        return String.format(Locale.US, "%.1f", value);
    }

    public static boolean isValidMacAddress(String macAddress) {
        return macAddress.matches(MAC_ADDRESS_IEEE_802_FORMAT_REGEX);
    }
}

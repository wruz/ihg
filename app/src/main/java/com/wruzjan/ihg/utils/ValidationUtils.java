package com.wruzjan.ihg.utils;

public class ValidationUtils {

    // Accept at least one and at most three digits followed by optional letter e.g valid strings:
    //1, 2A, 2a, 34, 52B, 52b, 356, 871F, 871f
    private static final String BUILDING_NUMBER_REGEX = "(?<![\\da-zA-Z])\\d{1,3}(?!\\d)[a-zA-Z]?(?![\\da-zA-Z])";

    public static boolean isMatchingBuildingNumberPattern(String buildingNumber) {
        return buildingNumber.matches(BUILDING_NUMBER_REGEX);
    }
}

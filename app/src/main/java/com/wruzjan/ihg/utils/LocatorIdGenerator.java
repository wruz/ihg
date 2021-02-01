package com.wruzjan.ihg.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.StreetAndIdentifier;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LocatorIdGenerator {

    private static final Map<Character, Character> LETTER_TO_DIGIT_MAP = Collections.unmodifiableMap(
            createLetterToDigitMapping()
    );

    private static Map<Character, Character> createLetterToDigitMapping() {
        HashMap<Character, Character> map = new HashMap<>();
        map.put('A', '1');
        map.put('B', '2');
        map.put('C', '3');
        map.put('D', '4');
        map.put('E', '5');
        map.put('F', '6');
        map.put('G', '7');
        map.put('H', '8');
        map.put('I', '9');
        return map;
    }

    @Nullable
    public String generate(
            @NonNull Address address,
            @NonNull StreetAndIdentifier streetAndIdentifier
    ) {
        Integer streetAndIdentifierId = address.getStreetAndIdentifierId();
        if (streetAndIdentifierId != null && streetAndIdentifierId > 0) {
            String buildingNumberPart;
            try {
                buildingNumberPart = formatNumber(address.getBuilding());
                if (TextUtils.isEmpty(buildingNumberPart)) {
                    return "";
                }
            } catch (NumberFormatException ignored) {
                buildingNumberPart = address.getBuilding();
                if (buildingNumberPart.length() == 1) {
                    buildingNumberPart = "0" + buildingNumberPart + "0";
                } else if (buildingNumberPart.length() == 2) {
                    if (StringUtils.isAlphanumeric(buildingNumberPart)) {
                        char letterPart = Character.toUpperCase(buildingNumberPart.charAt(1));
                        if (LETTER_TO_DIGIT_MAP.containsKey(letterPart)) {
                            buildingNumberPart = "0" + buildingNumberPart.toUpperCase().replace(letterPart, LETTER_TO_DIGIT_MAP.get(letterPart));
                        } else {
                            return "";
                        }
                    } else {
                        buildingNumberPart = buildingNumberPart + "0";
                    }
                } else if (buildingNumberPart.length() == 3) {
                    if (StringUtils.isAlphanumeric(buildingNumberPart)) {
                        char letterPart = Character.toUpperCase(buildingNumberPart.charAt(2));
                        if (LETTER_TO_DIGIT_MAP.containsKey(letterPart)) {
                            buildingNumberPart = buildingNumberPart.toUpperCase().replace(letterPart, LETTER_TO_DIGIT_MAP.get(letterPart));
                        } else {
                            return "";
                        }
                    } else {
                        return "";
                    }
                }
            }

            String flatNumberPart;
            try {
                flatNumberPart = formatNumber(address.getFlat());
                if (TextUtils.isEmpty(flatNumberPart)) {
                    return "";
                }
            } catch (NumberFormatException ignored) {
                flatNumberPart = address.getFlat();
                if (flatNumberPart.length() == 1) {
                    flatNumberPart = "0" + flatNumberPart + "0";
                } else if (flatNumberPart.length() == 2) {
                    if (StringUtils.isAlphanumeric(flatNumberPart)) {
                        char letterPart = Character.toUpperCase(flatNumberPart.charAt(1));
                        if (LETTER_TO_DIGIT_MAP.containsKey(letterPart)) {
                            flatNumberPart = "0" + flatNumberPart.toUpperCase().replace(letterPart, LETTER_TO_DIGIT_MAP.get(letterPart));
                        } else {
                            return "";
                        }
                    } else {
                        flatNumberPart = flatNumberPart + "0";
                    }
                } else if (flatNumberPart.length() == 3) {
                    if (StringUtils.isAlphanumeric(flatNumberPart)) {
                        char letterPart = Character.toUpperCase(flatNumberPart.charAt(2));
                        if (LETTER_TO_DIGIT_MAP.containsKey(letterPart)) {
                            flatNumberPart = flatNumberPart.toUpperCase().replace(letterPart, LETTER_TO_DIGIT_MAP.get(letterPart));
                        } else {
                            return "";
                        }
                    } else {
                        return "";
                    }
                }
            }

            return "0" + streetAndIdentifier.getStreetIdentifier() + buildingNumberPart + flatNumberPart;
        } else {
            return "";
        }
    }

    private String formatNumber(String number) throws NumberFormatException {
        int buildingNumber = Integer.parseInt(number);
        if (buildingNumber > 110) {
            return "";
        } else if (buildingNumber > 99) {
            switch (buildingNumber) {
                case 100:
                    return "991";
                case 101:
                    return "992";
                case 102:
                    return "993";
                case 103:
                    return "994";
                case 104:
                    return "995";
                case 105:
                    return "996";
                case 106:
                    return "997";
                case 107:
                    return "998";
                case 108:
                    return "999";
            }
        } else {
            return StringUtils.leftPad(number, 2, '0') + "0";
        }
        return "";
    }
}

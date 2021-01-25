package com.wruzjan.ihg.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.StreetAndIdentifier;

import org.apache.commons.lang3.StringUtils;

public class LocatorIdGenerator {

    @Nullable
    public String generate(
            @NonNull Address address,
            @NonNull StreetAndIdentifier streetAndIdentifier
    ) {
        Integer streetAndIdentifierId = address.getStreetAndIdentifierId();
        if (streetAndIdentifierId != null && streetAndIdentifierId > 0) {
            String buildingNumberPart = "";
            try {
                int buildingNumber = Integer.parseInt(address.getBuilding());
                if (buildingNumber > 110) {
                    return "";
                } else if (buildingNumber > 99) {
                    switch (buildingNumber) {
                        case 100:
                            buildingNumberPart = "99A";
                            break;
                        case 101:
                            buildingNumberPart = "99B";
                            break;
                        case 102:
                            buildingNumberPart = "99C";
                            break;
                        case 103:
                            buildingNumberPart = "99D";
                            break;
                        case 104:
                            buildingNumberPart = "99E";
                            break;
                        case 105:
                            buildingNumberPart = "99F";
                            break;
                        case 106:
                            buildingNumberPart = "99G";
                            break;
                        case 107:
                            buildingNumberPart = "99H";
                            break;
                        case 108:
                            buildingNumberPart = "99I";
                            break;
                        case 109:
                            buildingNumberPart = "99J";
                            break;
                        case 110:
                            buildingNumberPart = "99K";
                            break;
                    }
                } else {
                    buildingNumberPart = StringUtils.leftPad(address.getBuilding(), 2, '0') + "0";
                }
            } catch (NumberFormatException ignored) {
                buildingNumberPart = address.getBuilding();
            }
            return "0" + streetAndIdentifier.getStreetIdentifier() + buildingNumberPart + StringUtils.leftPad(address.getFlat(), 3, '0');
        } else {
            return "";
        }
    }
}

package com.wruzjan.ihg.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.StreetAndIdentifier;

public class LocatorIdGenerator {

    @Nullable
    public String generate(
            @NonNull Address address,
            @NonNull StreetAndIdentifier streetAndIdentifier
    ) {
        Integer streetAndIdentifierId = address.getStreetAndIdentifierId();
        if (streetAndIdentifierId != null && streetAndIdentifierId > 0) {
            return "0" + streetAndIdentifier.getStreetIdentifier() + address.getBuilding() + address.getFlat();
        } else {
            return "";
        }
    }
}

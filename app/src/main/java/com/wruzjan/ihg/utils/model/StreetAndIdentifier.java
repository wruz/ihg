package com.wruzjan.ihg.utils.model;

public class StreetAndIdentifier {

    private final int id;
    private final String streetName;
    private final int streetIdentifier;

    public StreetAndIdentifier(int id, String streetName, int streetIdentifier) {
        this.id = id;
        this.streetName = streetName;
        this.streetIdentifier = streetIdentifier;
    }

    public int getId() {
        return id;
    }

    public String getStreetName() {
        return streetName;
    }

    public int getStreetIdentifier() {
        return streetIdentifier;
    }
}

package com.wruzjan.ihg.utils.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DailyReport {

    @Nullable
    private final String locatorId;
    @NonNull
    private final String street;
    @NonNull
    private final String houseNumber;
    @Nullable
    private final String flatNumber;
    @NonNull
    private final String city;
    @NonNull
    private final String inspectionDate;
    @Nullable
    private final String previousInspectionDate;
    @NonNull
    private final String kitchen;
    @Nullable
    private final String kitchenComments;
    @NonNull
    private final String bathroom;
    @Nullable
    private final String bathroomComments;
    @NonNull
    private final String toilet;
    @Nullable
    private final String toiletComments;
    @NonNull
    private final String flue;
    @Nullable
    private final String flueComments;
    @NonNull
    private final String gas;
    @Nullable
    private final String gasComments;
    @Nullable
    private final String co2;
    @Nullable
    private final String commentsForUser;
    @Nullable
    private final String commentsForManager;
    @Nullable
    private final String smComments;

    private DailyReport(
            @Nullable String locatorId,
            @NonNull String street,
            @NonNull String houseNumber,
            @Nullable String flatNumber,
            @NonNull String city,
            @NonNull String inspectionDate,
            @Nullable String previousInspectionDate,
            @NonNull String kitchen,
            @Nullable String kitchenComments,
            @NonNull String bathroom,
            @Nullable String bathroomComments,
            @NonNull String toilet,
            @Nullable String toiletComments,
            @NonNull String flue,
            @Nullable String flueComments,
            @NonNull String gas,
            @Nullable String gasComments,
            @Nullable String co2,
            @Nullable String commentsForUser,
            @Nullable String commentsForManager,
            @Nullable String smComments) {
        this.locatorId = locatorId;
        this.street = street;
        this.houseNumber = houseNumber;
        this.flatNumber = flatNumber;
        this.city = city;
        this.inspectionDate = inspectionDate;
        this.previousInspectionDate = previousInspectionDate;
        this.kitchen = kitchen;
        this.kitchenComments = kitchenComments;
        this.bathroom = bathroom;
        this.bathroomComments = bathroomComments;
        this.toilet = toilet;
        this.toiletComments = toiletComments;
        this.flue = flue;
        this.flueComments = flueComments;
        this.gas = gas;
        this.gasComments = gasComments;
        this.co2 = co2;
        this.commentsForUser = commentsForUser;
        this.commentsForManager = commentsForManager;
        this.smComments = smComments;
    }

    @Nullable
    public String getLocatorId() {
        return locatorId;
    }

    @NonNull
    public String getStreet() {
        return street;
    }

    @NonNull
    public String getHouseNumber() {
        return houseNumber;
    }

    @Nullable
    public String getFlatNumber() {
        return flatNumber;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    @NonNull
    public String getInspectionDate() {
        return inspectionDate;
    }

    @Nullable
    public String getPreviousInspectionDate() {
        return previousInspectionDate;
    }

    @NonNull
    public String getKitchen() {
        return kitchen;
    }

    @Nullable
    public String getKitchenComments() {
        return kitchenComments;
    }

    @NonNull
    public String getBathroom() {
        return bathroom;
    }

    @Nullable
    public String getBathroomComments() {
        return bathroomComments;
    }

    @NonNull
    public String getToilet() {
        return toilet;
    }

    @Nullable
    public String getToiletComments() {
        return toiletComments;
    }

    @NonNull
    public String getFlue() {
        return flue;
    }

    @Nullable
    public String getFlueComments() {
        return flueComments;
    }

    @NonNull
    public String getGas() {
        return gas;
    }

    @Nullable
    public String getGasComments() {
        return gasComments;
    }

    @Nullable
    public String getCo2() {
        return co2;
    }

    @Nullable
    public String getCommentsForUser() {
        return commentsForUser;
    }

    @Nullable
    public String getCommentsForManager() {
        return commentsForManager;
    }

    @Nullable
    public String getSmComments() {
        return smComments;
    }

    public static final class Builder {
        private String locatorId;
        private String street;
        private String houseNumber;
        private String flatNumber;
        private String city;
        private String inspectionDate;
        private String previousInspectionDate;
        private String kitchen;
        private String kitchenComments;
        private String bathroom;
        private String bathroomComments;
        private String toilet;
        private String toiletComments;
        private String flue;
        private String flueComments;
        private String gas;
        private String gasComments;
        private String co2;
        private String commentsForUser;
        private String commentsForManager;
        private String smComments;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withLocatorId(String locatorId) {
            this.locatorId = locatorId;
            return this;
        }

        public Builder withStreet(String street) {
            this.street = street;
            return this;
        }

        public Builder withHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        public Builder withFlatNumber(String flatNumber) {
            this.flatNumber = flatNumber;
            return this;
        }

        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        public Builder withInspectionDate(String inspectionDate) {
            this.inspectionDate = inspectionDate;
            return this;
        }

        public Builder withPreviousInspectionDate(String previousInspectionDate) {
            this.previousInspectionDate = previousInspectionDate;
            return this;
        }

        public Builder withKitchen(String kitchen) {
            this.kitchen = kitchen;
            return this;
        }

        public Builder withKitchenComments(String kitchenComments) {
            this.kitchenComments = kitchenComments;
            return this;
        }

        public Builder withBathroom(String bathroom) {
            this.bathroom = bathroom;
            return this;
        }

        public Builder withBathroomComments(String bathroomComments) {
            this.bathroomComments = bathroomComments;
            return this;
        }

        public Builder withToilet(String toilet) {
            this.toilet = toilet;
            return this;
        }

        public Builder withToiletComments(String toiletComments) {
            this.toiletComments = toiletComments;
            return this;
        }

        public Builder withFlue(String flue) {
            this.flue = flue;
            return this;
        }

        public Builder withFlueComments(String flueComments) {
            this.flueComments = flueComments;
            return this;
        }

        public Builder withGas(String gas) {
            this.gas = gas;
            return this;
        }

        public Builder withGasComments(String gasComments) {
            this.gasComments = gasComments;
            return this;
        }

        public Builder withCo2(String co2) {
            this.co2 = co2;
            return this;
        }

        public Builder withCommentsForUser(String commentsForUser) {
            this.commentsForUser = commentsForUser;
            return this;
        }

        public Builder withCommentsForManager(String commentsForManager) {
            this.commentsForManager = commentsForManager;
            return this;
        }

        public Builder withSmComments(String smComments) {
            this.smComments = smComments;
            return this;
        }

        public DailyReport build() {
            return new DailyReport(locatorId, street, houseNumber, flatNumber, city, inspectionDate, previousInspectionDate, kitchen, kitchenComments, bathroom, bathroomComments, toilet, toiletComments, flue, flueComments, gas, gasComments, co2, commentsForUser, commentsForManager, smComments);
        }
    }
}



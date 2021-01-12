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
    private final String kitchenWindowsClosed;
    @NonNull
    private final String kitchenMicrovent;
    @Nullable
    private final String kitchenComments;
    @NonNull
    private final String bathroomWindowsClosed;
    @NonNull
    private final String bathroomMicrovent;
    @Nullable
    private final String bathroomComments;
    @NonNull
    private final String toiletWindowsClosed;
    @NonNull
    private final String toiletMicrovent;
    @Nullable
    private final String toiletComments;
    @NonNull
    private final String flueWindowsClosed;
    @NonNull
    private final String flueMicrovent;
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
    @Nullable
    private final String ventCount;
    @NonNull
    private final String gasCooker;
    @NonNull
    private final String bathroomBake;

    private DailyReport(Builder builder) {
        locatorId = builder.locatorId;
        street = builder.street;
        houseNumber = builder.houseNumber;
        flatNumber = builder.flatNumber;
        city = builder.city;
        inspectionDate = builder.inspectionDate;
        previousInspectionDate = builder.previousInspectionDate;
        kitchenWindowsClosed = builder.kitchenWindowsClosed;
        kitchenMicrovent = builder.kitchenMicrovent;
        kitchenComments = builder.kitchenComments;
        bathroomWindowsClosed = builder.bathroomWindowsClosed;
        bathroomMicrovent = builder.bathroomMicrovent;
        bathroomComments = builder.bathroomComments;
        toiletWindowsClosed = builder.toiletWindowsClosed;
        toiletMicrovent = builder.toiletMicrovent;
        toiletComments = builder.toiletComments;
        flueWindowsClosed = builder.flueWindowsClosed;
        flueMicrovent = builder.flueMicrovent;
        flueComments = builder.flueComments;
        gas = builder.gas;
        gasComments = builder.gasComments;
        co2 = builder.co2;
        commentsForUser = builder.commentsForUser;
        commentsForManager = builder.commentsForManager;
        smComments = builder.smComments;
        ventCount = builder.ventCount;
        gasCooker = builder.gasCooker;
        bathroomBake = builder.bathroomBake;
    }

    public static Builder newBuilder() {
        return new Builder();
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
    public String getKitchenWindowsClosed() {
        return kitchenWindowsClosed;
    }

    @Nullable
    public String getKitchenComments() {
        return kitchenComments;
    }

    @NonNull
    public String getBathroomWindowsClosed() {
        return bathroomWindowsClosed;
    }

    @Nullable
    public String getBathroomComments() {
        return bathroomComments;
    }

    @NonNull
    public String getToiletWindowsClosed() {
        return toiletWindowsClosed;
    }

    @Nullable
    public String getToiletComments() {
        return toiletComments;
    }

    @NonNull
    public String getFlueWindowsClosed() {
        return flueWindowsClosed;
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

    @NonNull
    public String getKitchenMicrovent() {
        return kitchenMicrovent;
    }

    @NonNull
    public String getBathroomMicrovent() {
        return bathroomMicrovent;
    }

    @NonNull
    public String getToiletMicrovent() {
        return toiletMicrovent;
    }

    @NonNull
    public String getFlueMicrovent() {
        return flueMicrovent;
    }

    @Nullable
    public String getVentCount() {
        return ventCount;
    }

    @NonNull
    public String getGasCooker() {
        return gasCooker;
    }

    @NonNull
    public String getBathroomBake() {
        return bathroomBake;
    }

    public static final class Builder {
        private String locatorId;
        private String street;
        private String houseNumber;
        private String flatNumber;
        private String city;
        private String inspectionDate;
        private String previousInspectionDate;
        private String kitchenWindowsClosed;
        private String kitchenMicrovent;
        private String kitchenComments;
        private String bathroomWindowsClosed;
        private String bathroomMicrovent;
        private String bathroomComments;
        private String toiletWindowsClosed;
        private String toiletMicrovent;
        private String toiletComments;
        private String flueWindowsClosed;
        private String flueMicrovent;
        private String flueComments;
        private String gas;
        private String gasComments;
        private String co2;
        private String commentsForUser;
        private String commentsForManager;
        private String smComments;
        private String ventCount;
        private String gasCooker;
        private String bathroomBake;

        private Builder() {
        }

        public Builder withLocatorId(String val) {
            locatorId = val;
            return this;
        }

        public Builder withStreet(String val) {
            street = val;
            return this;
        }

        public Builder withHouseNumber(String val) {
            houseNumber = val;
            return this;
        }

        public Builder withFlatNumber(String val) {
            flatNumber = val;
            return this;
        }

        public Builder withCity(String val) {
            city = val;
            return this;
        }

        public Builder withInspectionDate(String val) {
            inspectionDate = val;
            return this;
        }

        public Builder withPreviousInspectionDate(String val) {
            previousInspectionDate = val;
            return this;
        }

        public Builder withKitchenWindowsClosed(String val) {
            kitchenWindowsClosed = val;
            return this;
        }

        public Builder withKitchenMicrovent(String val) {
            kitchenMicrovent = val;
            return this;
        }

        public Builder withKitchenComments(String val) {
            kitchenComments = val;
            return this;
        }

        public Builder withBathroomWindowsClosed(String val) {
            bathroomWindowsClosed = val;
            return this;
        }

        public Builder withBathroomMicrovent(String val) {
            bathroomMicrovent = val;
            return this;
        }

        public Builder withBathroomComments(String val) {
            bathroomComments = val;
            return this;
        }

        public Builder withToiletWindowsClosed(String val) {
            toiletWindowsClosed = val;
            return this;
        }

        public Builder withToiletMicrovent(String val) {
            toiletMicrovent = val;
            return this;
        }

        public Builder withToiletComments(String val) {
            toiletComments = val;
            return this;
        }

        public Builder withFlueWindowsClosed(String val) {
            flueWindowsClosed = val;
            return this;
        }

        public Builder withFlueMicrovent(String val) {
            flueMicrovent = val;
            return this;
        }

        public Builder withFlueComments(String val) {
            flueComments = val;
            return this;
        }

        public Builder withGas(String val) {
            gas = val;
            return this;
        }

        public Builder withGasComments(String val) {
            gasComments = val;
            return this;
        }

        public Builder withCo2(String val) {
            co2 = val;
            return this;
        }

        public Builder withCommentsForUser(String val) {
            commentsForUser = val;
            return this;
        }

        public Builder withCommentsForManager(String val) {
            commentsForManager = val;
            return this;
        }

        public Builder withSmComments(String val) {
            smComments = val;
            return this;
        }

        public Builder withVentCount(String val) {
            ventCount = val;
            return this;
        }

        public Builder withGasCooker(String gasCooker) {
            this.gasCooker = gasCooker;
            return this;
        }

        public Builder withBathroomBake(String bathroomBake) {
            this.bathroomBake = bathroomBake;
            return this;
        }

        public DailyReport build() {
            return new DailyReport(this);
        }
    }
}



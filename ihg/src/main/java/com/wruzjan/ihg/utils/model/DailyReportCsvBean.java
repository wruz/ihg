package com.wruzjan.ihg.utils.model;

import com.opencsv.bean.CsvBindByPosition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DailyReportCsvBean {

    @CsvBindByPosition(position = 0)
    @NonNull
    private final String locatorId;
    @CsvBindByPosition(position = 1)
    @NonNull
    private final String street;
    @CsvBindByPosition(position = 2)
    @NonNull
    private final String houseNumber;
    @CsvBindByPosition(position = 3)
    @Nullable
    private final String flatNumber;
    @CsvBindByPosition(position = 4)
    @NonNull
    private final String city;
    @CsvBindByPosition(position = 5)
    @NonNull
    private final String inspectionDate;
    @CsvBindByPosition(position = 6)
    @Nullable
    private final String previousInspectionDate;
    @CsvBindByPosition(position = 7)
    @NonNull
    private final String kitchen;
    @CsvBindByPosition(position = 8)
    @Nullable
    private final String kitchenComments;
    @CsvBindByPosition(position = 9)
    @NonNull
    private final String bathroom;
    @CsvBindByPosition(position = 10)
    @Nullable
    private final String bathroomComments;
    @CsvBindByPosition(position = 11)
    @NonNull
    private final String toilet;
    @CsvBindByPosition(position = 12)
    @Nullable
    private final String toiletComments;
    @CsvBindByPosition(position = 13)
    @NonNull
    private final String flue;
    @CsvBindByPosition(position = 14)
    @Nullable
    private final String flueComments;
    @CsvBindByPosition(position = 15)
    @NonNull
    private final String gas;
    @CsvBindByPosition(position = 16)
    @Nullable
    private final String gasComments;
    @CsvBindByPosition(position = 17)
    @Nullable
    private final String co2;
    @CsvBindByPosition(position = 18)
    @Nullable
    private final String commentsForUser;
    @CsvBindByPosition(position = 19)
    @Nullable
    private final String commentsForManager;
    @CsvBindByPosition(position = 20)
    @Nullable
    private final String smComments;

    private DailyReportCsvBean(
            @NonNull String locatorId,
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

        public DailyReportCsvBean build() {
            return new DailyReportCsvBean(locatorId, street, houseNumber, flatNumber, city, inspectionDate, previousInspectionDate, kitchen, kitchenComments, bathroom, bathroomComments, toilet, toiletComments, flue, flueComments, gas, gasComments, co2, commentsForUser, commentsForManager, smComments);
        }
    }
}



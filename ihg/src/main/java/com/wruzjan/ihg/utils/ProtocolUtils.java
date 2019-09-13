package com.wruzjan.ihg.utils;

import com.wruzjan.ihg.utils.model.Protocol;
import com.wruzjan.ihg.utils.model.ProtocolNewPaderewskiego;

import androidx.annotation.NonNull;

public class ProtocolUtils {

    public static final int KITCHEN_ACCEPTANCE_THRESHOLD = 70;
    public static final int BATHROOM_ACCEPTANCE_THRESHOLD = 50;
    public static final int TOILET_ACCEPTANCE_THRESHOLD = 30;
    public static final int FLUE_ACCEPTANCE_THRESHOLD = 35;

    private static final float MEASURE_FACTOR = 0.36f;

    private ProtocolUtils() {
        // no-op
    }

    public static float calculateSiemanowiceKitchenAirflowWindowsClosed(@NonNull Protocol protocol) {
        double dimRound = protocol.get_kitchen_grid_dimension_round();
        float windowsClosed = protocol.get_kitchen_airflow_windows_closed();
        float dimX = protocol.get_kitchen_grid_dimension_x();
        float dimY = protocol.get_kitchen_grid_dimension_y();
        return calculateAirflow(dimRound, windowsClosed, dimX, dimY);
    }

    public static float calculateSiemanowiceBathroomAirflowWindowsClosed(@NonNull Protocol protocol) {
        double dimRound = protocol.get_bathroom_grid_dimension_round();
        float windowsClosed = protocol.get_bathroom_airflow_windows_closed();
        float dimX = protocol.get_bathroom_grid_dimension_x();
        float dimY = protocol.get_bathroom_grid_dimension_y();
        return calculateAirflow(dimRound, windowsClosed, dimX, dimY);
    }

    public static float calculateSiemanowiceToiletAirflowWindowsClosed(@NonNull Protocol protocol) {
        double dimRound = protocol.get_toilet_grid_dimension_round();
        float windowsClosed = protocol.get_toilet_airflow_windows_closed();
        float dimX = protocol.get_toilet_grid_dimension_x();
        float dimY = protocol.get_toilet_grid_dimension_y();
        return calculateAirflow(dimRound, windowsClosed, dimX, dimY);
    }

    public static float calculateNewPaderwskiegoKitchenAirflowWindowsClosed(@NonNull ProtocolNewPaderewskiego protocol) {
        double dimRound = protocol.get_kitchen_grid_dimension_round();
        float windowsClosed = protocol.get_kitchen_airflow_windows_closed();
        float dimX = protocol.get_kitchen_grid_dimension_x();
        float dimY = protocol.get_kitchen_grid_dimension_y();
        return calculateAirflow(dimRound, windowsClosed, dimX, dimY);
    }

    public static float calculateNewPaderwskiegoBathroomAirflowWindowsClosed(@NonNull ProtocolNewPaderewskiego protocol) {
        double dimRound = protocol.get_bathroom_grid_dimension_round();
        float windowsClosed = protocol.get_bathroom_airflow_windows_closed();
        float dimX = protocol.get_bathroom_grid_dimension_x();
        float dimY = protocol.get_bathroom_grid_dimension_y();
        return calculateAirflow(dimRound, windowsClosed, dimX, dimY);
    }

    public static float calculateNewPaderwskiegoToiletAirflowWindowsClosed(@NonNull ProtocolNewPaderewskiego protocol) {
        double dimRound = protocol.get_toilet_grid_dimension_round();
        float windowsClosed = protocol.get_toilet_airflow_windows_closed();
        float dimX = protocol.get_toilet_grid_dimension_x();
        float dimY = protocol.get_toilet_grid_dimension_y();
        return calculateAirflow(dimRound, windowsClosed, dimX, dimY);
    }

    public static float calculateSiemanowiceKitchenAirflowMicrovent(@NonNull Protocol protocol) {
        double dimRound = protocol.get_kitchen_grid_dimension_round();
        float windowsClosed = protocol.get_kitchen_airflow_microventilation();
        float dimX = protocol.get_kitchen_grid_dimension_x();
        float dimY = protocol.get_kitchen_grid_dimension_y();
        return calculateAirflow(dimRound, windowsClosed, dimX, dimY);
    }

    public static float calculateSiemanowiceBathroomAirflowMicrovent(@NonNull Protocol protocol) {
        double dimRound = protocol.get_bathroom_grid_dimension_round();
        float windowsClosed = protocol.get_bathroom_airflow_microventilation();
        float dimX = protocol.get_bathroom_grid_dimension_x();
        float dimY = protocol.get_bathroom_grid_dimension_y();
        return calculateAirflow(dimRound, windowsClosed, dimX, dimY);
    }

    public static float calculateSiemanowiceToiletAirflowMicrovent(@NonNull Protocol protocol) {
        double dimRound = protocol.get_toilet_grid_dimension_round();
        float windowsClosed = protocol.get_toilet_airflow_microventilation();
        float dimX = protocol.get_toilet_grid_dimension_x();
        float dimY = protocol.get_toilet_grid_dimension_y();
        return calculateAirflow(dimRound, windowsClosed, dimX, dimY);
    }

    public static float calculateNewPaderwskiegoKitchenAirflowMicrovent(@NonNull ProtocolNewPaderewskiego protocol) {
        double dimRound = protocol.get_kitchen_grid_dimension_round();
        float windowsClosed = protocol.get_kitchen_airflow_microventilation();
        float dimX = protocol.get_kitchen_grid_dimension_x();
        float dimY = protocol.get_kitchen_grid_dimension_y();
        return calculateAirflow(dimRound, windowsClosed, dimX, dimY);
    }

    public static float calculateNewPaderwskiegoBathroomAirflowMicrovent(@NonNull ProtocolNewPaderewskiego protocol) {
        double dimRound = protocol.get_bathroom_grid_dimension_round();
        float windowsClosed = protocol.get_bathroom_airflow_microventilation();
        float dimX = protocol.get_bathroom_grid_dimension_x();
        float dimY = protocol.get_bathroom_grid_dimension_y();
        return calculateAirflow(dimRound, windowsClosed, dimX, dimY);
    }

    public static float calculateNewPaderwskiegoToiletAirflowMicrovent(@NonNull ProtocolNewPaderewskiego protocol) {
        double dimRound = protocol.get_toilet_grid_dimension_round();
        float windowsClosed = protocol.get_toilet_airflow_microventilation();
        float dimX = protocol.get_toilet_grid_dimension_x();
        float dimY = protocol.get_toilet_grid_dimension_y();
        return calculateAirflow(dimRound, windowsClosed, dimX, dimY);
    }

    public static float calculateSiemanowiceFlueAirflowWindowsClosed(@NonNull Protocol protocol) {
        return protocol.get_flue_airflow_windows_closed() * 70.3f;
    }

    public static float calculateNewPaderwskiegoFlueAirflowWindowsClosed(@NonNull ProtocolNewPaderewskiego protocol) {
        return protocol.get_flue_airflow_windows_closed() * 70.3f;
    }

    public static float calculateSiemanowiceFlueAirflowMicrovent(@NonNull Protocol protocol) {
        return protocol.get_flue_airflow_microventilation() * 70.3f;
    }

    public static float calculateNewPaderwskiegoFlueAirflowMicrovent(@NonNull ProtocolNewPaderewskiego protocol) {
        return protocol.get_flue_airflow_microventilation() * 70.3f;
    }

    private static float calculateAirflow(double dimRound, float windowsClosed, float dimX, float dimY) {
        if (Double.compare(dimRound, 0.0) == 0) {
            return windowsClosed * dimX * dimY * MEASURE_FACTOR;
        } else {
            return (float) ((dimRound / 2) * ((dimRound) / 2) * Math.PI) * windowsClosed * MEASURE_FACTOR;
        }
    }
}

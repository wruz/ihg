package com.wruzjan.ihg.utils;

import android.os.Environment;

public class Utils {

    public static final String PREFS_NAME = "IhgPrefsFile";

    public final static String WORKER_NAME = "com.wruzjan.ihg.WORKER_NAME";

    public final static String TEMP_INSIDE = "com.wruzjan.ihg.TEMP_INSIDE";

    public final static String TEMP_OUTSIDE = "com.wruzjan.ihg.TEMP_OUTSIDE";

    public final static String TELEPHONE = "com.wruzjan.ihg.TELEPHONE";

    public final static String WIND_SPEED = "com.wruzjan.ihg.WIND_SPEED";

    public final static String WIND_DIRECTION = "com.wruzjan.ihg.WIND_DIRECTION";

    public final static String PRESSURE = "com.wruzjan.ihg.PRESSURE";

    public final static String WINDOWS_ALL = "com.wruzjan.ihg.WINDOWS_ALL";

    public final static String WINDOWS_MICRO = "com.wruzjan.ihg.WINDOWS_MICRO";

    public final static String WINDOWS_VENT = "com.wruzjan.ihg.WINDOWS_VENT";

    public final static String WINDOWS_NO_MICRO = "com.wruzjan.ihg.WINDOWS_NO_MICRO";

    public final static String ADDRESS_ID = "com.wruzjan.ihg.ADDRESS_ID";

    public final static String PROTOCOL_ID = "com.wruzjan.ihg.PROTOCOL_ID";

    public final static String EDIT_FLAG = "com.wruzjan.ihg.EDIT";

    public static final String SIGNATURE_PATH
            = Environment.getExternalStorageDirectory().toString() + "/IHG/graphics/signature.png";

    public static final String DEBUG_TAG = "IHG_DEBUG";

    public static final int USER_COMMENTS_LENGTH = 512;

//  shared preferences keys
    public static final String OUTSIDE_TEMPERATURE_SIEMIANOWICE = "tempOutsideSiemianowice";

    public static final String INSIDE_TEMPERATURE_PADEREWSKIEGO = "tempInside";

    public static final String OUTSIDE_TEMPERATURE_PADEREWSKIEGO = "tempOutside";

    public static final String WIND_SPEED_PADEREWSKIEGO = "windSpeed";

    public static final String WIND_DIRECTION_PADEREWSKIEGO = "windDirectionPosition";

    public static final String PREASURE_PADEREWSKIEGO = "pressure";

    public static final String OUTSIDE_TEMPERATURE_NOWY_PADEREWSKIEGO = "tempOutsideNowyPaderewskiego";

    public static final String WORKER_POSITION = "workerPosition";

    public static final String STREET_MANUAL = "street_manual";

    public static final String STREET_ID = "street_id";

    public static final String CITY = "city";

    public static final String DISTRICT = "district";

    public static final String PRINTER_MAC = "printerMac";

    public static final String PRINTER_POSITION = "printerPosition";

    public static final String PREF_SIEMANOWICE_COMPANY_ADDRESS = "PREF_SIEMANOWICE_COMPANY_ADDRESS";

    public static final String PREF_SIEMANOWICE_PROTOCOL_TYPE = "PREF_SIEMANOWICE_PROTOCOL_TYPE";

    public static final String PREF_NEW_PADEREWSKIEGO_COMPANY_ADDRESS = "PREF_NEW_PADEREWSKIEGO_COMPANY_ADDRESS";

    public static final String PREF_NEW_PADEREWSKIEGO_PROTOCOL_TYPE = "PREF_NEW_PADEREWSKIEGO_PROTOCOL_TYPE";
}
